<h1> CodeOwnership </h1>

Aplicação Java que analisa projetos do Git e determina <i>ownership<i> e <i>expertise<i> de código através de informações de logs do Git. A aplicação é voltada para projetos desenvolvidos na disciplina de Laboratório de Programação II na Universidade Federal de Campina Grande (UFCG), mas pode ser estendida a qualquer projeto que faz uso de Git para controle de versões. 
Para utilizar a aplicação basta executar o comando abaixo e seguir as intruções.

	$ java -jar codeOwnership.jar

Caso o arquivo `codeOwnership.jar` esteja desatualizado, é possível executar o projeto como uma aplicação Java. Para obter os resultados da análise, basta indicar o caminho para a pasta do projeto que se deseja analisar (na qual está contido o diretório `.git`). Nessa mesma pasta, é necessário ter um arquivo `students.json` contendo uma associação entre nomes e aliases dos contribuidores do projeto da seguinte forma:

```
// students.json
{
    "students": [
      {
        "name": "Student 1",
        "aliases": [
          "Username 1",
          "Username 2"
        ]
      },
      {
        "name": "Student 2",
        "aliases": [
          "Username 3",
	  "Username 4",
	  "Username 5"
        ]
      }, ...
    ]
  }
```
Fazemos uso desse arquivo `.json` já que muitas vezes os usuários de sistemas Git ficam associados a mais de um username quando não configuram corretamente suas máquinas.

Caso o processo de análise ocorra com sucesso, um arquivo `analysis-result.tsv` será gerado na pasta `outputs`. Ele deverá seguir o [modelo que está contido neste repositório](./outputs/analysis-result.example.tsv).

É utilizada a biblioteca [JGit](https://www.eclipse.org/jgit/) para obter as informações dos arquivos de log do repositório. 
A análise de expertise é feita atráves de palavras chaves. Portando, só funcionará corretamente em códigos escritos na linguagem Java. 
