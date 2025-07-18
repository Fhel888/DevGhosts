# Projeto DevGhosts - Construção de uma ERP (simulação de uma empresa de consultoria)

Este projeto simula uma ERP de Empresa de consultoria empresarial, que desenvolve métodos com estratégias personalizadas e direcionada ao perfil de cada empresa contratante.
Nesta ERP será possivel subir processos em pdf, visualizar e excluir, sendo organizada conforme o setor.

## Como Executar o Projeto (Offline com Docker)

Este projeto utiliza Docker e Docker Compose para criar um ambiente de servidor web (Apache + PHP) e um banco de dados (MySQL) de forma isolada e offline.

### Pré-requisitos

- [Docker](https://www.docker.com/get-started) instalado na sua máquina.

### Passos para Execução

1.  **Inicie os contêineres Docker:**
    Abra um terminal na pasta raiz do projeto (onde o arquivo `docker-compose.yml` está localizado) e execute o comando:
    ```bash
    docker-compose up -d
    ```

3.  **Acesse a aplicação:**
    Sua ERP estará disponível no seu navegador em: **`http://localhost:8080/index.php`**

Pronto! Sua ERP está online (localmente) e pronta para ser usada.