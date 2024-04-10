
# Aplicação multithread com sockets

O projeto consiste no desenvolvimento de um servidor multithread, onde o mesmo aceitará a conexão de vários clientes.

Projeto desenvolvido por: Gustavo Henrique Araújo Barbosa e Kelvin Fernandes


## Configurações para execução do projeto:

- É utilizado um banco de dados MySQL;

- Para criação do banco de dados, pode-se usar a seguinte Query:

- CREATE TABLE IF NOT EXISTS images (
    id_image INT AUTO_INCREMENT PRIMARY KEY,
    name_image VARCHAR(255),
    img BLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

- Deve ser criado um arquivo com o seguinte nome na raiz do projeto:
    config.properties

    E nesse arquivo deve conter as seguintes informações: 
    - database.url = jdbc:mysql://{ip do banco}:{porta do banco}/{nome da tabela}
    - database.user = usuario do banco
    - database.password = senha do banco

    Na aplicação contém um arquivo de exemplo para você se guiar;





## Funcionalidades: 

 - 1 - Download de uma imagem
    - Caso existam, serão listadas as imagens presentes em um banco de dados local;
    - O cliente irá digitar o ID da imagem que ele deseja baixar e assim a imagem será salva em uma pasta assets localmente;

- 2 - Upload de imagem
    - O cliente irá digitar o nome da imagem que ele deseja enviar para o banco de dados, lembrando que precisa ser uma imagem .png;
    - A imagem será salva no banco de dados local;

- 3 Excluir imagem
    - Assim como no download, serão listadas as imagens presentes no banco de dados da aplicação e o cliente digitará o ID daquela imagem que ele deseja excluir.
    - A imagem só será apagada de fato do banco de dados, caso a imagem do ID selecionado esteja presente localmente em sua máquina!

