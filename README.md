# AWS-LEARN - Projeto desenvolvido para aprendizado da tecnologia

Este sistema foi projetado para subir o ambiente AWS com as tecnologias ECS, Fargate, SNS, SQS, RDS, DynamoDB e S3.
Também foi desenvolvido dois microsserviços para utilizar as tecnologias mensionadas, o primeiro (aws-project-code) utilizado para salvar um CRUD básico de
produtos disparando mensagens de e-mail e projetando notificações via SQS. O segundo microsserviço (aws_learn_consumer) serviu como consumer dessas mensagens
e utilizou o DynamoDB para gravar essas informações. Também nele foi feita uma parte para aprendizado fazendo uso do S3.

## Requisitos
Para montar o ambiente do projeto é necessário:

- Java 17
- NVM v14.5.0
- Docker
- Gradle
- AWS CLI v2

Para subir o ambiente AWS execute:

`$ cdk deploy --all --parameters Rds:databasePassword=[PASSWORD]`

## Configuração do Backend
Este projeto foi desenvolvido utilizando a arquitetura Spring e conta com vários módulos para seu completo funcionamento.

Para que seja devidamente preparado o ambiente para sua execução, deve-se executar o comando abaixo:

`$ mvn clean install`
