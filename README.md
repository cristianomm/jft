# jft


## Submódulos

* **connector** 
> Realiza a comunicação entre cliente e match engine.
* **core**
> 
* **data**
> 
* **db**
> Módulo responsável por realizar a comunicação com o banco de dados. Utiliza uma interface genérica para controle de transações, consultas, inserção e remoção de objetos. Os parâmetros de conexão devem ser ajustados no arquivo hibernate.cfg.xml
* **engine**
> Matching engine implementado utilizando como referência a documentação disponibilizada pela B3: [EntryPoint: Order Entry Messaging](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/EntryPoint/EntryPointMessagingGuidelines2.9.pdf) e 
[UMDF - FIX/FAST Market Data Messaging Specification](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/UMDF/Sinal%20de%20difusão/UMDF_MarketDataSpecification_v2.1.0.pdf)
* **init**
> Inicializa o banco de dados, criando tabelas e inserindo dados básicos.
* **model**
> 
* **logging**
> Funcionalidades de log, com auxílio da biblioteca log4j.
* **messaging**
> Funcionalidades para tratamento de mensagens que utilizam o protocolo FIX. Utiliza a biblioteca QuickFIXJ para auxiliar no processo de codificação e decodificação de mensagens no protocolo FIX.
* **services**
> Disponibiliza 3 serviços básicos para realizar negociação:
  - Trading: Gerenciamento de ordens (inserção, cancelamento e atualização de ordens, bem como o tratamento de mensagens do tipo ExecutionReport).
  - Security: Mantém dados sobre os instrumentos podem ser negociados.
  - MarketData: Processa todas mensagens de MarketData e as mantém disponíveis para utilização pelo cliente.
* **ui**
> Interface cliente para envio de ordens.
