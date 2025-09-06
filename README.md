# 🚀 UFAPE Web Crawler

![Duke](https://user-images.githubusercontent.com/45864414/90345135-19bcb300-dff5-11ea-81f9-d2a166b82088.png)

Um projeto Java para obter dados de uma URL específica, seus links e subdomínios! Este README fornecerá uma
visão geral do projeto, instruções de instalação, uso e muito mais.

---

## 💡 Sobre o Projeto

Este projeto foi desenvolvido visando analisar o site institucional
da [Universidade Federal do Agreste de Pernambuco](https://ufape.edu.br/), aplicando conhecimento adquirido sobre Grafos
na Disciplina Algoritmos e Estrutura de Dados II.

---

## 🛠️ Tecnologias Utilizadas

* [Java 24+](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html) - Linguagem de programação
* [Maven](https://maven.apache.org/) - Gerenciador de dependências e build
* [Jackson](https://github.com/FasterXML/jackson) - Biblioteca para JSON parser
* [JUnit](https://junit.org/) - Framework para testes Unitários
* [Mockito](https://site.mockito.org/) - Framework de testes com dados mockados
* [Docker](https://www.docker.com/) - Plataforma de containerização

---

## 🛫 Como Começar

Siga estas instruções para colocar o projeto em funcionamento em sua máquina local para fins de desenvolvimento e teste.

### Pré-requisitos

Certifique-se de ter instalado em sua máquina:

* JDK 24 ou superior
* Maven 3.9.9 ou superior
* Docker (para rodar a API REST Crawler, caso não tenha instalado, será necessário o download do executável
  em [Ufape Crawler Golang Releases](https://github.com/nettojulio/ufape-crawler-golang/releases) conforme o seu
  sistema operacional)

### Instalação

1. **Clone o repositório:**

   HTTPS

   ```bash
   git clone https://github.com/nettojulio/ufape-crawler.git
   ```

   SSH

   ```bash
   git clone git@github.com:nettojulio/ufape-crawler.git
   ```

2. **Configure o projeto:**
   Altere o arquivo `src/main/java/br/com/julioneto/config/CrawlerConfig.java` caso queira personalizar as
   configurações:
   ```java
   // Exemplo de CrawlerConfig.java
   
   // Configurações da aplicação
   public static final String URL_INICIAL = "https://ufape.edu.br/"; // URL inicial para iniciar a extração
   public static final int MAX_DEPTH = Integer.MAX_VALUE; // Profundidade máxima dos links visitados a partir da URL inicial

   // Configurações da API REST Crawler
   public static final String API_BASE_URL = "http://localhost:8080"; // URL da API REST Crawler
   public static final int API_TIMEOUT_SECONDS = 300; // Timeout da API em segundos para obter dados de um link
   public static final boolean API_REMOVE_FRAGMENTS = false; // Remover fragments das URLs encontradas
   public static final List<String> API_ALLOWED_DOMAINS = List.of("ufape.edu.br"); // Domínios permitidos para serem visitados
   public static final boolean API_COLLECT_SUBDOMAINS = true; // Coletar informacoes de subdominios a partir da URL inicial
   public static final boolean API_LOWER_CASE_URL = false; // Converter todas as URLs encontradas para minúsculas
   public static final boolean API_CAN_RETRY = true; // Habilita sistema de tentativas
   public static final int API_MAX_ATTEMPTS = 3; // Quantidade máxima de tentativas

   // Configurações de execução do projeto
   public static final boolean USE_THREADS = false; // Habilita a execução em modo concorrente (true) ou sequencial (false)
   ```

3. **Instale as dependências com Maven:**
   ```bash
   mvn clean install
   ```

4. **Configuração da API Crawler (com Docker):**
   ```bash
   docker-compose up -d
   ```
   Ou rode o executável conforme seu sistema operacional.

5. **Execute o Projeto (Opcional):**
   ```bash
   java -jar target/ufape-crawler-1.0-SNAPSHOT.jar
   ```
   O projeto será inicializado e exibirá logs de execução.

   Após encerrar, será gerado um arquivo JSON com o seguinte formato:
   ```text
   grafo_salvo_yyyy-MM-dd'T'HH-mm-ss.json
   ```

6. **Visualização (Opcional)**

   Caso queira visualizar de forma gráfica:
    - Renomeie o arquivo gerado para `grafo_salvo.json`
    - Abra o arquivo `docs/index.html` em seu navegador (caso as permissões de CORS permitam) ou execute em um servidor
      local (Ex: [Live Server](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer))
    
---

## 🧪 Rodando os Testes

Para rodar os testes unitários, execute o seguinte comando na raiz do projeto:

```bash
   mvn clean test
```

Para visualizar a cobertura de testes, execute:

```bash
   mvn clean verify
```

E acesse o seguinte arquivo

```text
target/site/jacoco/index.html
```
