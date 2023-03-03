# Módulo de Envio de Planos de Trabalho do Programa de Gestão (sistema SUSEP)
Autoria:CADE/ME

Contêiner do módulo de Envio de Planos de Trabalho do Programa de Gestão (PGD). O Software tem como objetivo realizar o gerenciamento (envio/consulta) dos Planos de Trabalhos do Programa de Gestão (Projeto de Transformação Digital).

**Observação: esta API foi desenvolvida com base no Sistema da SUSEP.**

[Sistema do Programa de Gestão - PG_ME_SUSEP](https://github.com/spbgovbr/Sistema_Programa_de_Gestao_Susep)

**Observação: é necessário criar as tables e views da API PGD. Favor, utilizar os scripts SQL abaixo:**

**Observação: é altamente recomendável criar um novo banco de dados para as tabelas API PGD (databaseName=APIPGD). As view são criadas na mesma estrutura do banco de dados do programa de gestão (databaseName=PGD). PGD é o nome do seu banco de dados do Programa de Gestão.**

[CREATE_TABLES_SQL_SERVER_SUSEP - PG_ME_SUSEP](https://github.com/spbgovbr/Sistema_Programa_de_Gestao_Susep/blob/main/Script%201%20-%20CREATE_TABLES_SQL_SERVER_SUSEP.sql)

[VIEWS_API_PGD_SUSEP - PG_ME_SUSEP](https://github.com/spbgovbr/Sistema_Programa_de_Gestao_Susep/blob/main/Script%202%20-%20VIEWS_API_PGD_SUSEP.sql)

**Observação: é necessário verificar se as views da API PGD foram criadas no schema dbo.*.**

DATABASE_SCHEMA_NAME: informar o nome do schema do banco de dados do programa de gestão. Para maiores detalhes, favor consultar a seção **Parâmetros do contêiner**.

APIPGDME_URL: informar a url de conexão com a API PGD ME sem /docs.

**URL Homologação:** http://hom.api.programadegestao.economia.gov.br

**URL Produção:** https://api-programadegestao.economia.gov.br

# Releases de versão da imagem

**1.0: versão mais recente**
- Melhoria no desempenho das páginas:
  - API PG - Enviar Planos
  - API PG - Planos Enviados
  - Logs
- Implementação completa do Cadastro de um Novo Usuário:
  - Removido a opção cadastrar senha para um novo usuário
  - Envio de e-mail para um novo usuário com senha
- Painel de Alteração de Senha:
  - Implementação da funcionalidade Alterar Senha
  - Necessário: 
    - Adicionar a nova funcionalidade em Funções (Nova Função > Descrição: Alterar Senha > Página Acesso: PAINEL_ALTERAR_SENHA > Salvar)
    - Sair e efetuar o login novamente para atualizar a nova Função
    - Adicionar a nova funcionalidade em Perfis (Manter Funções do Perfil (ícone engrenagem) > Função: Alterar Senha > Adicionar Função > Alterar: Sim > Visualizar: Sim > Salvar)
    - Sair e efetuar o login novamente para carregar as permissões do Perfil
- Usuários:
  - Implementação da funcionalidade Resetar Senha
- Versão estável

# Atualização de versão da imagem

**Observação: caso deseje obter atualizações de novas versões da imagem, por gentileza, remova a imagem local e efetue um novo download diretamente do repositório:**

```shell
docker pull cadegovbr/apiprogramagestaosusep
```
# Atualização de Segurança: Apache Log4j 2 CVE-2021-44228 vulnerability

### Update Dockerfile:
ADD standalone.sh with parameter -Dlog4j.formatMsgNoLookups=true

Fonte: [Get the latest on Apache Log4j 2 CVE-2021-44228 vulnerability from Docker](https://www.docker.com/blog/apache-log4j-2-cve-2021-44228/)

### Update Maven log4j2.version:
```shell
<properties>
    <log4j2.version>2.17.1</log4j2.version>
</properties>
```
Fonte: [Log4J2 Vulnerability and Spring Boot](https://spring.io/blog/2021/12/10/log4j2-vulnerability-and-spring-boot)

## Passos Iniciais

As instruções a seguir são informações necessárias para a utilização de contêiners docker.

### Pré-requisitos

Para executar este contêiner, você precisará do docker instalado.

* [Windows](https://docs.docker.com/windows/started)
* [OS X](https://docs.docker.com/mac/started/)
* [Linux](https://docs.docker.com/linux/started/)

### Parâmetros do contêiner

Parâmetros disponíveis para o seu contêiner.

```shell
docker run -p 8080:8080 -p 9990:9990 -e WILDFLY_USERNAME="admin" -e WILDFLY_PASSWORD="4dm1n" \ 
-e DATABASE_APIPGD_USER="user" -e DATABASE_APIPGD_PASSWORD="passwd" -e DATABASE_APIPGD_URL="jdbc:sqlserver://host:1433;databaseName=APIPGD" \ 
-e DATABASE_PGD_USER="user" -e DATABASE_PGD_PASSWORD="passwd" -e DATABASE_PGD_URL="jdbc:sqlserver://host:1433;databaseName=PGD" \ 
-e DATABASE_SCHEMA_NAME="PGD" -e APIPGDME_URL="url" -e APIPGDME_AUTH_USER="user" -e APIPGDME_AUTH_PASSWORD="passwd" \
-e MAIL_RECIPIENTS_TO="aaa@email.com,bbb@email.com" -e MAIL_RECIPIENTS_CC="aaa@email.com,bbb@email.com" -e MAIL_HOST="smtp" -e MAIL_PORT="25" \
-e MAIL_USER="user" -e MAIL_PASSWORD="passwd" -e MAIL_SMTP_AUTH="true" -e MAIL_SMTP_STARTTLS_ENABLE="false" \ 
-e MAIL_SMTP_STARTTLS_REQUIRED="false" -e MAIL_SMTP_SSL_ENABLE="false" -e TZ="America/Sao_Paulo" cadegovbr/apiprogramagestaosusep
```

#### Variáveis de Ambiente

* `WILDFLY_USERNAME` - Usuário de administração do WildFly.
* `WILDFLY_PASSWORD` - Senha do Usuário de administração do WildFly.
* `DATABASE_APIPGD_URL` - URL para conexão com o banco de dados da API PGD.
* `DATABASE_APIPGD_USER` - Usuário do banco de dados da API PGD.
* `DATABASE_APIPGD_PASSWORD` - Senha do Usuário do banco de dados da API PGD.
* `DATABASE_PGD_URL` - URL para conexão com o banco de dados do PGD.
* `DATABASE_SCHEMA_NAME` - Nome do schema do banco de dados do PGD.
* `DATABASE_PGD_USER` - Usuário do banco de dados do PGD.
* `DATABASE_PGD_PASSWORD` - Senha do Usuário do banco de dados do PGD.
* `APIPGDME_URL` - Url de conexão com a API PGD ME.
* `APIPGDME_AUTH_USER` - Usuário de conexão com a API PGD ME.
* `APIPGDME_AUTH_PASSWORD` - Senha do Usuário de conexão com a API PGD ME.
* `MAIL_RECIPIENTS_TO` - E-mails dos destinatários que irão receber mensagens da API (for multiple recipients = [aaa@email.com](mailto:aaa@email.com),[bbb@email.com](mailto:bbb@email.com)).
* `MAIL_RECIPIENTS_CC` (opcional) - E-mails dos destinatários (em cópia) que irão receber mensagens da API (for multiple recipients = [aaa@email.com](mailto:aaa@email.com),[bbb@email.com](mailto:bbb@email.com)).
* `MAIL_HOST` - Endereço IP ou DNS do Host SMTP.
* `MAIL_PORT` - Porta do Host SMTP.
* `MAIL_USER` - E-mail do usuário.
* `MAIL_PASSWORD` - Senha de e-mail do usuário.
* `MAIL_SMTP_AUTH` - Flag indicando se há autenticação SMTP. ["true", "false"]
* `MAIL_SMTP_STARTTLS_ENABLE` - Flag indicando se há TLS habilitado no SMTP. ["true", "false"]
* `MAIL_SMTP_STARTTLS_REQUIRED` - Flag indicando se o SMTP requer TLS. ["true", "false"]
* `MAIL_SMTP_SSL_ENABLE` - Flag indicando se há SSL habilitado no SMTP. ["true", "false"]
* `TZ` - Timezone. Horário Brasileiro: "America/Sao_Paulo"

## Arquitetura do Módulo de Envio de Planos de Trabalho do Programa de Gestão (PGD)

* SpringBoot 2.3.9.RELEASE
* JSF 2+

## Encontre-nos

* [GitHub](https://github.com/spbgovbr/Sistema_Envio_Planos_PGD_Susep)
* [Docker](https://hub.docker.com/u/cadegovbr)

## Versionamento

Nós utilizamos o [GitLab](https://github.com/spbgovbr/Sistema_Envio_Planos_PGD_Susep) para versionamento.

## Autores

* **Conselho Administrativo de Defesa Econômica - CADE** - [CADE](https://www.gov.br/cade/pt-br)
* **Ministério da Economia - ME** - [ME](https://www.gov.br/economia/pt-br)

## Licença

Todos os softwares utilizados neste projeto possuem licença de código aberto.
