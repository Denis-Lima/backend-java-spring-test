## Como rodar

### Clone o repositório

```bash
git clone https://github.com/Denis-Lima/backend-java-spring-test.git
cd backend-java-spring-test
```

#### OBS: Os comandos a seguir (`./mvnw`) podem ser trocador por `.\mvnw.cmd` caso esteja no Windows.

### Instalação das dependências

1.
    - Certifique-se de usar Java 11 ou superior
2.
    - Rode o seguinte comando (ou troque pelo seu maven local):

```bash
./mvnw clean install
```

### Para iniciar a aplicação

```bash
./mvnw spring-boot:run
```

Para consultar a documentação da API, acesse: `http://localhost:8080/api/swagger-ui/index.html`

### Para rodar os testes isoladamente

```bash
./mvnw test
```

A cobertura de testes será gerada na pasta `target/site/jacoco/index.html`