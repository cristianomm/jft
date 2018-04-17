# jft
O objetivo deste projeto é a construção de uma matching engine para a realização de testes e validação de sistemas de negociação.
O projeto foi dividido em módulos que contém funcionalidades específicas e auxiliam na construção de dois sistemas: Matching Engine e Sistema cliente.

A Matching Engine está sendo construida tendo como referência o [PUMA Trading System](http://www.bmfbovespa.com.br/pt_br/servicos/negociacao/puma-trading-system-bm-fbovespa/) e toda a documentação fornecida pelo site da B3 [Entry Point](http://www.bmfbovespa.com.br/pt_br/servicos/negociacao/puma-trading-system-bm-fbovespa/para-desenvolvedores-e-vendors/entrypoint-entrada-de-ofertas/) e [UMDF](http://www.bmfbovespa.com.br/pt_br/servicos/negociacao/puma-trading-system-bm-fbovespa/para-desenvolvedores-e-vendors/umdf-sinal-de-difusao/) como base de conhecimento para a costrução do sistema.

O Sistema cliente tem por finalidade oferecer uma plataforma básica e simplificada de negociação para treinamento e validação de sistemas de negociação.

## Módulos

* **connector** 
> Realiza a comunicação e também o recebimento de mensagens FIX entre cliente e matching engine. Mantém os buffers de mensagens de MarketData e Negociação.
* **data**
> Este módulo disponibiliza funcionalidades para extração de dados de negociação de arquivos que contém o histórico de negociações realizadas. Também contém funcionalidades para recebimento de dados via DDE.
* **db**
> Módulo responsável por realizar a comunicação com o banco de dados. Utiliza uma interface genérica para controle de transações, consultas, inserção e remoção de objetos. Os parâmetros de conexão devem ser ajustados no arquivo hibernate.cfg.xml
* **engine**
> Matching engine implementada utilizando como referência a documentação disponibilizada pela B3: [EntryPoint: Order Entry Messaging](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/EntryPoint/EntryPointMessagingGuidelines2.9.pdf) e 
[UMDF - FIX/FAST Market Data Messaging Specification](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/UMDF/Sinal%20de%20difusão/UMDF_MarketDataSpecification_v2.1.0.pdf)
* **init**
> Inicializa o banco de dados, criando tabelas e inserindo dados básicos.
* **model**
> Contém os objetos básicos utilizados em cada módulo.
* **logging**
> Funcionalidades de log, com auxílio da biblioteca log4j.
* **messaging**
> Contém funcionalidades para realizar o tratamento de mensagens que utilizam o protocolo FIX. Com o intuito de simplificar a implementação, apenas o protocolo FIX (v 4.4) está sendo utilizado, tanto para o envio e recebimento de ordens quanto para o MarketData. 
Utiliza a API QuickFIX/J para auxiliar no processo de codificação e decodificação de mensagens no protocolo FIX.
* **services**
> Disponibiliza 3 serviços básicos para realizar negociação:

> *Trading:* Gerenciamento de ordens (inserção, cancelamento e atualização de ordens, bem como o tratamento de mensagens do tipo ExecutionReport).

> *Security:* Mantém dados sobre os instrumentos que podem ser negociados.

> *MarketData:* Processa todas mensagens de MarketData e as mantém disponíveis para utilização pelo cliente.
* **ui**
> Interface cliente com telas básicas para envio de ordens e acompanhamento do mercado.


Todas as referências de cada módulo estão contidas no respectivo arquivo pom.xml.


