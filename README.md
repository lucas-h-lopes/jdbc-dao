# jdbc-dao

Este repositório é uma implementação de um [CRUD](https://developer.mozilla.org/pt-BR/docs/Glossary/CRUD) em Java, usando a [API JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/) para conexão com o banco de dados.

O projeto foi estruturado conforme o padrão [Data Access Object](https://www.oracle.com/java/technologies/data-access-object.html), que consiste em __separar__ a lógica de manipulação de dados no banco de dados da lógica de negócios. De acordo com esse padrão, cada entidade deve possuir também uma classe DAO responsável por manipular os registros dessa entidade no banco de dados, realizando operações como inserção, deleção, atualização e consulta.

# Instalação ⚙️

Para rodar o programa localmente, execute os seguintes passos:

1. Clone esse repositório:

   ``git clone https://github.com/lucas-h-lopes/jdbc-dao.git``


2. Acesse a pasta principal:

   ``cd jdbc-dao``


3. Adicione um arquivo __db.properties__ com as configurações abaixo

    - user= {nome de usuário criado para acessar o banco}

    - password= {senha de usuário criada para acessar o banco}

    - dburl=jdbc:mysql://localhost:{SUA_PORTA_AQUI}/{NOME_DATABASE_AQUI}?allowPublicKeyRetrieval=true&useSSL=false``


4. No seu banco de dados MySQL, importe as tabelas através do arquivo: [database.sql](https://drive.google.com/file/d/1ozKRCVRkR2fZa7BPp6Fp-Lj6RwYGLcZX/view).


5. Vá até a pasta __src__:

   ``cd src``


5. Compile e execute a classe Program:

``javac application/Program.java``

``java application/Program``


# Visualização do Projeto 👀

O programa inicia exibindo o menu inicial, que contém todas as operações que podem ser feitas. Para acessá-las, informe o número correspondente.

![menu](https://raw.githubusercontent.com/lucas-h-lopes/jdbc-dao/main/img/menu.png)

Após selecionar uma operação válida, é exibido então um menu específico, caso sua operação possa ser aplicada para Departament e Seller:

![menu-especifico](https://raw.githubusercontent.com/lucas-h-lopes/jdbc-dao/main/img/menu-especifico.png)

Ao selecionar se deseja realizar a ação com Department ou Seller, o resultado pode ser visualizado na tela:

![resultado](https://raw.githubusercontent.com/lucas-h-lopes/jdbc-dao/main/img/resultado-consulta.png)

O programa permanece em execução enquanto a operação informada for diferente de 0, ou seja, é possível realizar múltiplas consultas sem a necessidade de executar o programa novamente:

![programa-encerrado](https://raw.githubusercontent.com/lucas-h-lopes/jdbc-dao/main/img/parando-execucao.png)
