# Teste Prático para Desenvolvedor Pleno Back-End em Java e Spring Boot

Este repositório contém o teste prático para a vaga de Desenvolvedor Pleno Back-End em Java e Spring Boot.

## Descrição do Projeto

Desenvolver uma aplicação de gerenciamento de tarefas (todo list) com as seguintes funcionalidades:

1. **Cadastro de Usuários**:
   - Criar, editar, excluir e listar usuários.
   - Cada usuário deve ter um nome e nível (admin, user).
2. **Gerenciamento de Tarefas**:
   - Criar, editar, excluir e listar tarefas.
   - Cada tarefa deve ter um título, descrição, data de criação, data de vencimento, status (pendente, em andamento, concluída) e um usuário associado.
3. **Filtros e Ordenação**:
   - Permitir que as tarefas sejam filtradas por status.
   - Permitir que as tarefas sejam ordenadas por data de vencimento.
4. **Associação de Tarefas a Usuários**
   - Permitir que tarefas sejam atribuídas a usuários específicos.
   - Permitir que as tarefas de um usuário específico sejam listadas.
   
### Aplicação de Testes unitários
   - O metodos devem ser testados com JUnit

## Requisitos Técnicos

- Java 11 ou superior
- Spring Boot
- Banco de Dados Relacional (H2 para facilitar os teste)
- JPA/Hibernate
- Maven
- Spring Security
- Spring Data JPA
- JUnit e Mockito para testes

## Instruções para Implementação

1. Clone o repositório:
   ```bash
   git clone https://github.com/ManagerThalles/backend-java-spring-test.git
   cd backend-java-spring-boot-test
2. Siga as instruções no arquivo `requirements.md` para detalhes sobre a implementação.

## Configuração Automática

Para facilitar a configuração do ambiente, você pode usar o script `setup.sh` incluído no repositório. 

### Passos para Configuração

1. Clone o repositório:
    ```bash
    git clone https://github.com/ManagerThalles/backend-java-spring-test.git
    cd backend-java-spring-boot-test
    ```

2. Dê permissão de execução ao script:
    ```bash
    chmod +x setup.sh
    ```

3. Execute o script:
    ```bash
    ./setup.sh
    ```

O script irá verificar se você tem Java 11 e Maven instalados, instalar as dependências do Maven e iniciar a aplicação Spring Boot.

## Envio do Projeto
O candidato deve enviar o link do repositório (GitHub) contendo o código-fonte do projeto, junto com um arquivo README.md explicando como configurar e executar a aplicação.

Boa sorte!
