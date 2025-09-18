

<p align="center">
  <a href="#portugues">Português</a> • <a href="#english">English</a>
</p>

---
<a id="portugues"></a>

# Personal Finance App (Aplicativo de Finanças Pessoais)

Este é o meu Aplicativo de Finanças Pessoais, criado para auxiliar no rastreamento de despesas e gerenciamento financeiro. O projeto foi desenvolvido com foco no público brasileiro, portanto, a interface do usuário está em português.

O código-fonte, comentários e a documentação técnica seguem o padrão internacional em inglês, visando as melhores práticas de desenvolvimento e facilitando a colaboração.

---

## Funcionalidades

- Autenticação (Spring Security e Encriptação BCrypt)
- Rastreamento de assinaturas mensais
- Adicionar cartões de crédito e outros métodos de pagamento (complicente com o PCI DCSS)

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Docker
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Spring Scheduler
- Quartz

---

## Estrutura do Projeto

- No momento, existe somente a parte de web. O diretório root do Spring, localiza-se no caminho **web/Finance**

---

## Rodando a Aplicação


### Pré-requisitos:

    - Java JDK 17
    - MariaDB 12 ou MySQL8
    - Docker
    - Git

---


1. **Clone o repositório**
    ```bash
    git clone https://github.com/luis-gustavo12/personal-finance-app.git
    cd personal-finance-app
    ```

2. **Preencha as informações em um arquivo .env**
    - Você irá precisar:
        - Uma chave de encriptação AES
        - Uma chave para tokens JWT
        - Credenciais da Stripe
        - Um servidor SMTP


    - Rode o seguinte comando para copiar o .env.example
    ```bash
    cp .env.example .env
    ```

    1) Para gerar as chaves tanto de AES quando de JWT, você pode rodar o seguinte comando no Linux:
    ```bash
    openssl rand -base64 32
    ```
    2) Para as credenciais da Stripe, você pode pegá-las [aqui](https://docs.stripe.com/keys). Pegue as chaves públicas e privadas.

    3) Para o servidor SMTP, você pode utilizar o Mailbox, ou o GMail. Recomendo o [Mailbox](https://mailtrap.io/) pela simplicidade, mas requer cadastro, e também, é um sandbox. Você também pode utilizar o [Brevo](https://www.brevo.com/). Ambas opções gratuitas.

    - Após isso, preencher de acordo
        ```env
        JWT_SECRET_KEY=JWT_SECRET_KEY

        AES_SECRET_KEY=AES_SECRET_KEY

        CARDGATEWAY_PUBLIC_KEY=STRIPE_PUBLIC_KEY
        CARDGATEWAY_PRIVATE_KEY=STRIPE_PRIVATE_KEY

        SPRING_MAIL_HOST=www.smtp.com
        SPRING_MAIL_PORT=587
        SPRING_MAIL_USERNAME=EMAIL
        SPRING_MAIL_PASSWORD=PASSWORD
        ```


3. **Execute o Docker para criar o banco de dados, e rodar a aplicação**

    ```bash
    docker-compose up --build -d
    ```

## Observações

*   **Idioma:** A interface do usuário está em português (PT-BR) para melhor experiência do público brasileiro. O código e a documentação técnica estão em inglês.



---
<br>
<a id="english"></a>
<!-- English Content Starts Here -->

# Personal Finance App

This is my Personal Finance App, created to help track expenses and manage personal finances. The user interface is in Portuguese, as the project was initially developed with a Brazilian audience in mind.

The source code, comments, and technical documentation follow international standards in English, aiming for best development practices and ease of collaboration.

---

## Features

- Authentication
- Monthly subscriptions tracking
- Add credit cards, and other payment methods
- Add income from Account to Account

---

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Spring Mail

---

## Notes

*   **Language:** The user interface is in Portuguese (PT-BR) to best serve the Brazilian audience. The code and technical documentation are in English.
