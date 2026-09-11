# Respostas da Avaliação Prática - PetVida

Nome: Gabriel Soares Teodoro
NN: 37

---

## Parte B — Planejamento da camada de controle

| Ação | Tipo de requisição | URL completa | Mapeamento (anotação) | Template |
| :--- | :--- | :--- | :--- | :--- |
| Mostrar a ficha do animal | GET | http://localhost:8080/ficha_37 | @GetMapping("/ficha_37") | ficha.html |
| Mostrar a ficha do tutor | GET | http://localhost:8080/tutor_37 | @GetMapping("/tutor_37") | tutor.html |
| Mostrar o resumo da clínica | GET | http://localhost:8080/resumo_37 | @GetMapping("/resumo_37") | resumo.html |
| Cadastrar um novo animal | POST | não existe | não existe | não existe |

**B.1**
O Spring utiliza a URL para decidir qual método de controller deve responder à requisição. O verbo HTTP isoladamente (como GET) não é suficiente porque todas as três rotas de leitura utilizam exatamente o mesmo verbo HTTP GET.

---

## Parte C — Depuração

| Item | É defeito? | Sintoma observado — mensagem literal | Causa | Correção aplicada |
| :--- | :--- | :--- | :--- | :--- |
| 1 | SIM | `A component required a bean of type 'br.edu.iftm.petvida.controller.ConsultaController' that could not be found.` | A classe não possuía a anotação `@Controller`, logo o Spring não registrou a classe como um Bean Spring nem escaneou seus mapeamentos. | Adicionada a anotação `@Controller` sobre a classe. |
| 2 | SIM | `Whitelabel Error Page (Type=Not Found, Status=404)` | O caminho `"consulta"` não começava com `/`. O Spring MVC exige `/consulta` para o mapeamento relativo correto na raiz. | Alterado para `@GetMapping("/consulta")`. |
| 3 | NÃO | (Funcionou após ajustar os itens 1 e 2) | O método `buscarPorId(1)` é uma chamada válida do repositório para carregar os dados. | Nenhuma alteração feita. |
| 4 | SIM | `Circular view path [consulta]: would dispatch back to the current handler URL [/consulta] again` | Retornar `"consulta.html"` instrui a renderização como se fosse uma URL física e não o nome lógico da view Thymeleaf. | Alterado o retorno para `"consulta"`. |
| 5 | SIM | O HTML renderiza literalmente o texto `${animal.especie}` na tela em vez de interpolar a variável. | Faltou utilizar o atributo `th:text="${bicho.especie}"` do Thymeleaf; além disso, a chave adicionada no Controller chamava-se `bicho` e não `animal`. | Alterado para `<p th:text="${bicho.especie}"></p>`. |

**D.1**
*(Cole aqui a saída do comando `git log --oneline` do seu terminal)*

**D.2**
A linha com `th:text="${bicho.nome}"` é processada pelo Thymeleaf no lado do servidor, que avalia a expressão OGNL/SpEL contra a Model, substitui o corpo do elemento e gera o HTML limpo. A linha com `${animal.especie}` não utiliza nenhuma diretiva do Thymeleaf (`th:*`), portanto é tratada como texto HTML estático puro, sendo enviada de forma literal sem nenhum tipo de interpolação.

**D.3**
O item 3 (`model.addAttribute("bicho", repository.buscarPorId(1));`) está correto pois o método `buscarPorId` de `AnimalRepository` aceita um `int` como argumento e retorna a entidade esperada para preencher a chave `"bicho"` no `Model`. Isso foi verificado na execução prática do projeto ao consertar os itens 1, 2, 4 e 5, onde a busca do ID 1 funcionou com sucesso.

---

## Parte D — Análise crítica de uma resposta de IA

**E.1**

| Item | Classificação | Justificativa |
| :--- | :--- | :--- |
| (a) | INCORRETA | O `JdbcTemplate` por si só faz a conversão de `SQLException` para a hierarquia de `DataAccessException` do Spring runtime; a anotação `@Repository` é para escaneamento e tradução adicional de exceções genéricas na camada DAO. |
| (b) | INCORRETA | O `@GetMapping` restringe o mapeamento exclusivamente para requisições do tipo HTTP GET. Requisições POST ou PUT para essa URL resultarão em erro `405 Method Not Allowed`. |
| (c) | PARCIALMENTE CORRETA | Substitui o conteúdo pela expressão da Model, mas se a variável não existir/for nula no Model, o Thymeleaf renderiza o campo vazio em branco (apagando o texto estático fallback), a não ser que haja um tratamento de exceção ou operador de elvis `?:`. |
| (d) | INCORRETA | O objeto `Model` do parâmetro do método é a interface `org.springframework.ui.Model` que funciona como um mapa de transporte de dados para a View, enquanto a classe `Animal` representa o domínio do negócio/modelo de dados. |
| (e) | INCORRETA | Chamar o `JdbcTemplate` diretamente no Controller funciona do ponto de vista técnico, porém viola a arquitetura em camadas e o princípio de responsabilidade única. |

**E.2 — Prova experimental**
* Para (b): Alterei temporariamente para `@GetMapping("/teste")` e enviei uma requisição `POST` via cliente HTTP. Recebi resposta HTTP `405 Method Not Allowed`, provando que `@GetMapping` atende apenas requisições GET.
* Para (c): Criei a tag `<p th:text="${itemInexistente}">Texto Padrão</p>`. Ao abrir a página no navegador sem passar `itemInexistente` no Model, o texto "Texto Padrão" desapareceu e a tag ficou vazia, provando que ele substitui o conteúdo por nulo e não retém o HTML padrão.

E.3 — Prova documental
Documentação oficial da Spring Framework (Data Access - DataAccessException):
* URL: `https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html`
* Citação: "JdbcTemplate catches SQLExceptions and translates them to the generic, more informative exception hierarchy defined in the org.springframework.dao package."

**E.4**
1. Caso 1 (Variável nula / `null`): A chave existe na Model, porém possui valor nulo. O Thymeleaf renderiza a tag HTML totalmente vazia, limpando o texto fallback estático.
2. Caso 2 (Atributo inexistente/Expressão inválida): Tenta-se acessar uma propriedade de um objeto nulo (ex: `${animalInexistente.nome}`). O Thymeleaf lança uma exceção em tempo de execução `TemplateProcessingException` (ou `PropertyNotFoundException`) resultando em Erro 500.

---

## Parte E — Rastreamento e arquitetura

**F.1**

| Etapa | Onde acontece | O que acontece |
| :--- | :--- | :--- |
| 1 | Navegador | Envia requisição HTTP GET para `http://localhost:8080/resumo_37`. |
| 2 | `PetController.java` / `resumo()` | O Handler Mapping mapeia a requisição para o método `resumo()`. |
| 3 | `AnimalRepository.java` | Invoca `contarAnimais()`, `mediaIdade()` e `animalMaisVelho()` via `JdbcTemplate`. |
| 4 | Banco de Dados H2 | Executa os SQLs (`SELECT COUNT(*)`, `SELECT AVG(...)`, `SELECT nome...`) e retorna os resultados. |
| 5 | `PetController.java` | Formata a média para 2 casas decimais, formata a data/hora e injeta as Strings no objeto `Model`. |
| 6 | Engine Thymeleaf | Processa o arquivo `resumo.html`, substituindo os atributos `th:text` pelas Strings formatadas da `Model`. |
| 7 | Navegador | Recebe a resposta com o HTML final totalmente montado e o renderiza para o usuário. |

**F.2**
* Linha: `PetController.java`, linha 42 (`String mediaFormatted = String.format("%.2f", media);`).
* Preservação de Responsabilidade: A camada Controller e o Java tratam das regras de apresentação/formatação dos tipos de dados antes da exibição. A View (Template HTML) é mantida estritamente como um componente de exibição visual burro (dumb view), separando completamente a lógica de código e regras de transformação da estrutura visual do documento HTML.

**F.3**
O erro ocorre no momento ao subir a aplicação (durante o *startup* do contexto do Spring Boot). O Spring mapeia e monta a tabela de rotas (`HandlerMapping`) na inicialização do servidor. A mensagem de erro observada é:
`IllegalStateException: Ambiguous mapping. Cannot map 'petController' method ... to {GET [/resumo_37]}: There is already 'petController' bean method mapped.`

**F.4**
Se a aplicação fizesse `SELECT *` para processar a média em memória no Java com 500 mil registros:
Haveria o tráfego de centenas de megabytes de dados do banco de dados para a aplicação Java pela rede, sobrecarregando a memória da JVM e causando alto custo de I/O. Com o `SELECT AVG(idade) FROM animal`, apenas um único valor numérico (alguns bytes) trafega na rede.

---

## Parte F — Defesa escrita do seu código

**G.1**
```java
public Animal buscarPorId(int id) {
    String sql = "SELECT a.id_animal, a.nome AS animal_nome, a.especie, a.idade, " +
                 "t.id_tutor, t.nome AS tutor_nome, t.telefone " +
                 "FROM animal a JOIN tutor t ON a.tutor_id_tutor = t.id_tutor " +
                 "WHERE a.id_animal = ?";
    
    return jdbc.queryForObject(sql, (rs, rowNum) -> {
        Tutor tutor = new Tutor(
            rs.getInt("id_tutor"),
            rs.getString("tutor_nome"),
            rs.getString("telefone")
        );
        return new Animal(
            rs.getInt("id_animal"),

**G.2**
Teria dificuldade em reescrever as linhas que ditam o retorno dos parâmetros por que eu provavelmente me atrapalharia na hora de identificar e designar corretamente esses parâmetros.
            rs.getString("animal_nome"),
            rs.getString("especie"),
            rs.getInt("idade"),
            tutor
        );
    }, id);
}
