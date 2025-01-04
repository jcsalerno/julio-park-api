# Sistema de Gerenciamento de Estacionamento

## Descrição

Este projeto é uma **API RESTful** desenvolvida para o gerenciamento de estacionamento, incluindo funcionalidades para cadastro de usuários, clientes, vagas e controle de entradas e saídas de veículos. A autenticação é feita através de **JSON Web Token (JWT)**, garantindo que todas as ações sejam realizadas de forma segura.

A API permite que administradores gerenciem usuários, clientes e vagas, enquanto os clientes podem consultar seus próprios dados e realizar check-ins e check-outs de estacionamento.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Security** (para autenticação com JWT)
- **JPA** (para persistência no banco de dados)
- **PostgreSQL** (banco de dados relacional)
- **Maven** (gerenciador de dependências)
- **JUnit** e **Mockito** (para testes)

## Funcionalidades

### 1. **Gestão de Usuários**

- **Cadastro de Usuários**: O sistema permite que administradores criem usuários com e-mail único, senha (mínimo 6 caracteres) e perfil (administrador ou cliente).
- **Autenticação**: O login é realizado utilizando o e-mail e senha. Após a autenticação, o sistema retorna um **JWT** para autenticar as requisições subsequentes.
- **Alteração de Senha**: Usuários podem alterar suas senhas, mas apenas quando autenticados.

### 2. **Gestão de Clientes**

- **Cadastro de Cliente**: Após o cadastro de um usuário, ele pode se cadastrar como cliente fornecendo dados como nome completo e CPF (único).
- **Consulta de Clientes**: Administradores podem listar todos os clientes, enquanto clientes só podem consultar seus próprios dados.

### 3. **Gestão de Vagas**

- **Cadastro de Vagas**: Administradores podem cadastrar vagas com código único e status de "livre" ou "ocupada".
- **Consulta de Vagas**: Vagas podem ser consultadas por código.

### 4. **Gestão de Estacionamento**

- **Entrada e Saída de Veículos**: O sistema permite que clientes estacionem veículos informando o CPF e os administradores gerenciem as entradas e saídas. A data de entrada, placa do veículo, marca, modelo, cor e o código da vaga são registrados.
- **Cálculo de Custos**: O sistema calcula o custo do estacionamento com base no tempo de permanência:
    - **Primeiros 15 minutos**: R$ 5,00
    - **Primeiros 60 minutos**: R$ 9,25
    - **Após 60 minutos**: R$ 1,75 para cada faixa de 15 minutos adicionais.

- **Descontos**: Clientes ganham 30% de desconto no próximo estacionamento após completar 10 estacionamentos.

### 5. **Recursos Restritos e Permissões**

- **Check-in e Check-out**: Apenas administradores podem realizar o check-in e check-out de veículos.
- **Acesso aos Dados de Estacionamento**: Administradores podem consultar todos os dados de estacionamento, enquanto clientes só podem acessar os seus próprios registros.