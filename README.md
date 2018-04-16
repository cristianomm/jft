# jft


## Submódulos

###### connector 
> Realiza a comunicação entre cliente e match engine.
###### core
> 
###### data
> 
###### db
> Módulo responsável por realizar a comunicação com o banco de dados. Utiliza uma interface genérica para controle de transações, consultas, inserção e remoção de objetos. Os parâmetros de conexão devem ser ajustados no arquivo hibernate.cfg.xml
###### engine
> Matchengine implementado conforme especificado nos documentos [EntryPoint: Order Entry Messaging](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/EntryPoint/EntryPointMessagingGuidelines2.9.pdf) e 
[UMDF - FIX/FAST Market Data Messaging Specification](https://github.com/cristianomm/jft/tree/master/doc/process/bmfbovespa/UMDF/Sinal%20de%20difusão/UMDF_MarketDataSpecification_v2.1.0.pdf)
###### init
> Inicializa o banco de dados, criando tabelas e inserindo dados básicos.
###### model
> 
###### logging
> Funcionalidades de log, com auxílio da biblioteca log4j.
###### messaging
> Funcionalidades para tratamento de mensagens que utilizam o protocolo FIX. Utiliza a biblioteca QuickFIXJ para auxiliar no processo de codificação e decodificação de mensagens no protocolo FIX.
###### services
> Contém 3 serviços básicos para realizar negociação:
- **Trading:** Gerenciamento de ordens
- **Security:** 
- **MarketData:** 
###### ui
> Interface cliente para envio de ordens.
