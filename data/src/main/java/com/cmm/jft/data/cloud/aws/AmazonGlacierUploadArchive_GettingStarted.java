package com.cmm.jft.data.cloud.aws;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacier;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.AmazonGlacierClientBuilder;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManagerBuilder;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class AmazonGlacierUploadArchive_GettingStarted {

	static final int ONE_MB = 1024 * 1024;
	public static String vaultName = "b3-logs";
	public static String archiveToUpload = "/home/cristiano/GitHub/Repos/jft/file/Accounts.csv";

	private static String access_key = " AKIATZMQYO2ZD6VRCUMK";
	private static String secret_key = "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";

	public static AmazonGlacierClient client;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(access_key, secret_key);

		AmazonGlacier glacier =  AmazonGlacierClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withRegion(Regions.SA_EAST_1).build();

		List<DescribeVaultOutput> vaults = glacier.listVaults(new ListVaultsRequest("-")).getVaultList();
		vaults.forEach(v->System.out.println(v));
		
		//System.exit(0);
		
		File file = new File(archiveToUpload);
		System.out.println(toHex(computeSHA256TreeHash(file)));
		
		UploadArchiveRequest uploadRequest = new UploadArchiveRequest();
		uploadRequest.setVaultName(vaultName);
		uploadRequest.setChecksum(toHex(computeSHA256TreeHash(file)));
		uploadRequest.setBody(new FileInputStream(file));
		uploadRequest.setContentLength(file.length());
		
		UploadArchiveResult result = glacier.uploadArchive(uploadRequest);
		System.out.println(result);
		
	}

	/**
	 * Computes the SHA-256 tree hash for the given file
	 * 
	 * @param inputFile
	 *            a File to compute the SHA-256 tree hash for
	 * @return a byte[] containing the SHA-256 tree hash
	 * @throws IOException
	 *             Thrown if there's an issue reading the input file
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] computeSHA256TreeHash(File inputFile) throws IOException,
	NoSuchAlgorithmException {

		byte[][] chunkSHA256Hashes = getChunkSHA256Hashes(inputFile);
		return computeSHA256TreeHash(chunkSHA256Hashes);
	}

	/**
	 * Computes a SHA256 checksum for each 1 MB chunk of the input file. This
	 * includes the checksum for the last chunk even if it is smaller than 1 MB.
	 * 
	 * @param file
	 *            A file to compute checksums on
	 * @return a byte[][] containing the checksums of each 1 MB chunk
	 * @throws IOException
	 *             Thrown if there's an IOException when reading the file
	 * @throws NoSuchAlgorithmException
	 *             Thrown if SHA-256 MessageDigest can't be found
	 */
	public static byte[][] getChunkSHA256Hashes(File file) throws IOException,
	NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		long numChunks = file.length() / ONE_MB;
		if (file.length() % ONE_MB > 0) {
			numChunks++;
		}

		if (numChunks == 0) {
			return new byte[][] { md.digest() };
		}

		byte[][] chunkSHA256Hashes = new byte[(int) numChunks][];
		FileInputStream fileStream = null;

		try {
			fileStream = new FileInputStream(file);
			byte[] buff = new byte[ONE_MB];

			int bytesRead;
			int idx = 0;

			while ((bytesRead = fileStream.read(buff, 0, ONE_MB)) > 0) {
				md.reset();
				md.update(buff, 0, bytesRead);
				chunkSHA256Hashes[idx++] = md.digest();
			}

			return chunkSHA256Hashes;

		} finally {
			if (fileStream != null) {
				try {
					fileStream.close();
				} catch (IOException ioe) {
					System.err.printf("Exception while closing %s.\n %s", file.getName(),
							ioe.getMessage());
				}
			}
		}
	}

	/**
	 * Computes the SHA-256 tree hash for the passed array of 1 MB chunk
	 * checksums.
	 * 
	 * This method uses a pair of arrays to iteratively compute the tree hash
	 * level by level. Each iteration takes two adjacent elements from the
	 * previous level source array, computes the SHA-256 hash on their
	 * concatenated value and places the result in the next level's destination
	 * array. At the end of an iteration, the destination array becomes the
	 * source array for the next level.
	 * 
	 * @param chunkSHA256Hashes
	 *            An array of SHA-256 checksums
	 * @return A byte[] containing the SHA-256 tree hash for the input chunks
	 * @throws NoSuchAlgorithmException
	 *             Thrown if SHA-256 MessageDigest can't be found
	 */
	public static byte[] computeSHA256TreeHash(byte[][] chunkSHA256Hashes)
			throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		byte[][] prevLvlHashes = chunkSHA256Hashes;

		while (prevLvlHashes.length > 1) {

			int len = prevLvlHashes.length / 2;
			if (prevLvlHashes.length % 2 != 0) {
				len++;
			}

			byte[][] currLvlHashes = new byte[len][];

			int j = 0;
			for (int i = 0; i < prevLvlHashes.length; i = i + 2, j++) {

				// If there are at least two elements remaining
				if (prevLvlHashes.length - i > 1) {

					// Calculate a digest of the concatenated nodes
					md.reset();
					md.update(prevLvlHashes[i]);
					md.update(prevLvlHashes[i + 1]);
					currLvlHashes[j] = md.digest();

				} else { // Take care of remaining odd chunk
					currLvlHashes[j] = prevLvlHashes[i];
				}
			}

			prevLvlHashes = currLvlHashes;
		}

		return prevLvlHashes[0];
	}

	/**
	 * Returns the hexadecimal representation of the input byte array
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A String containing Hex characters
	 */
	public static String toHex(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);

		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xFF);

			if (hex.length() == 1) {
				// Append leading zero.
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString().toLowerCase();
	}
}
