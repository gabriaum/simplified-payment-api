# PicPay Simplificado

Este projeto foi desenvolvido inspirado no desafio de backend do PicPay, com algumas melhorias e adaptações. Implementado como projeto pessoal/passa-tempo. O objetivo é demonstrar uma solução RESTful simplificada para gerenciamento de usuários, autenticação, depósitos e transferências entre carteiras, respeitando as regras de negócio propostas no desafio.

> Nota: este repositório é um exercício pessoal. Não se trata de um produto para produção.

## Resumo do Desafio (objetivo)

Criar um serviço que permita cadastrar usuários (pessoas e lojistas), autenticar, depositar e transferir valores entre carteiras. Regras principais:

- CPF e e-mail únicos por usuário.
- Lojistas só recebem transferências (não enviam).
- Verificar saldo antes de transferir.
- Antes de concluir uma transferência, consultar um serviço autorizador externo.
- Enviar notificações (serviço externo) ao receber/enviar pagamento. O serviço pode estar indisponível.
- A operação de transferência deve ser transacional.

Este projeto implementa os fluxos essenciais e as integrações simuladas com serviços externos.

## Melhorias e decisões de implementação

As principais melhorias/decisões adotadas em relação ao enunciado original:

- Testes unitários: adicionei testes unitários focados nas regras críticas (autenticação/registro e fluxo de transferência).
- Tratamento de erro e handlers: exceções de domínio (usuário não encontrado, saldo insuficiente, autorização falhou, notificação indisponível) com mapeamento consistente para respostas HTTP.
- Separação de responsabilidades: factories para conversão de entidades/DTOs, serviços distintos para autorização e notificação, repositórios JPA.
- Segurança básica: uso de geração de token JWT (via `TokenGenerator`) e storage de senha com BCrypt (via `CryptographyService`).
- Transacionalidade: o fluxo de transferência é anotado com `@Transactional` para garantir rollback em caso de falhas.
- Estrutura RESTful e validação via annotations (ex.: DTOs com `@NotNull`).

Estas decisões tornam o projeto mais testável, claro e semelhante a uma aplicação real, mantendo a simplicidade pedida no desafio.

## Tecnologias

- Java 21
- Spring Boot (Web, Data JPA, Security)
- JUnit 5 + Mockito (testes)
- MySQL (configurável via variáveis de ambiente)
- JWT via jjwt
- Lombok (para reduzir boilerplate)

## Endpoints principais

- Autenticação:
  - POST /api/v1/user/auth/login - login com CPF e senha.
  - POST /api/v1/user/auth/register - cadastro de usuário.

- Transações:
  - POST /api/v1/transaction/transfer - efetua transferência (usuário autenticado com role USER).
    - Exemplo de payload:
      ```json
      {
        "value": 100.0,
        "payeeId": 15,
        "description": "Pagamento"
      }
      ```
  - GET /api/v1/transaction/history - histórico (requisição para perfil de manager/admin).
  - GET /api/v1/transaction/my-history - histórico do usuário autenticado.

Observação: os contratos seguem a API implementada no código. O endpoint original do desafio (`POST /transfer`) foi mantido semanticamente; aqui a rota é versionada em `/api/v1/transaction/transfer`.

## Integrações externas (mocks)

- Serviço autorizador: a implementação consulta `https://util.devi.tools/api/v2/authorize` para validar autorização de transferência (conforme enunciado).
- Serviço de notificação: existe uma abstração `NotificationService` que usa `RestTemplate` para enviar notificações; por padrão no código de exemplo a URL usada é local (pode ser alterada para apontar a mock `https://util.devi.tools/api/v1/notify` ou para um stub local).

Se for necessário testar integrações reais, aponte o `RestTemplate`/URLs para os mocks externos ou rode um stub local para a rota de notificação.

## Testes

Adicionei testes unitários focados nas regras mais críticas:

- `TransactionServiceTest` — cobre fluxo de transferência (sucesso, saldo insuficiente, destinatário inválido, autorização negada).
- `AuthServiceTest` — cobre autenticação e registro (sucesso e falhas esperadas).

Os testes usam Mockito para isolar dependências externas (repositórios, serviços HTTP, geração de token, criptografia) e podem ser estendidos para cobrir mais casos.

## Limitações e observações

- Projeto implementado como estudo/passa-tempo. Não foi endurecido para produção (ex.: políticas de segurança, rate limiting, monitoramento ou auditoria avançada).
- A URL de notificação no código está apontando para um endpoint local; ajuste conforme necessidade para utilizar o mock público recomendado pelo desafio.
- Algumas funcionalidades adicionais (painel administrativo, paginação avançada, métricas) podem ser adicionadas conforme necessidade.