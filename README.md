# Watch

### Visão Geral:

A principal finalidade do aplicativo é compartilhar opinião em uma linha do tempo - **Simulada, ou seja, totalmente standalone** - como o twitter, sobre filmes e/ou séries que o usuário adicionar à sua lista, ou fazer um esquema (por capítulos, se for série, ou notas, se for filme). 
Caso não tenha uma conta, poderá se cadastrar. É possível também fazer comentários à publicações de outros usuários.

### Biblioteca utilizada:
Para que a adição de posts e séries fosse facilitada foi incluído ![esta biblioteca](https://github.com/yavski/fab-speed-dial). Observe a imagem abaixo:

![screenshot_20190212-134210](https://user-images.githubusercontent.com/16518399/52652108-9e94b100-2ed4-11e9-80d0-cf0123cedc4f.png)

### Requisitos e Restrições
* API 19: Android 4.4 (KitKat);
* Aplicativo desenvolvido utilizando a linguagem de programação Kotlin;
* Banco de dados utlizado: ObjectBox.

### Funcionalidades:

* Depois de cadastrado e logado o usuário pode adicionar filmes e/ou séries de sua preferência ao aplicativo;
* Editar e remover filmes/séries;
* Compartilhar a opinião sobre o filme/série adicionado;
* Fazer anotações, caso queira relembrar o que aconteceu naquele momento do filme ou episódio da série;
* Editar e remover anotações;
* Compartilhar diretamente as anotações (pode alterá-las antes de publicar);
* Comentar publicações de outros usuários;
* Deletar publicações caso tenha compartilhado as mesmas;
* Deletar comentários caso seja o dono da publicação.

### Diagrama de Classes Simplificado:

![diagrama de classes projeto final](https://user-images.githubusercontent.com/16518399/52596527-a4888480-2e37-11e9-8d70-12863d5aa59a.png)

### Mapa do Aplicativo (Storyboard):
O aplicativo possui 8 atividades (telas) descritas abaixo:

![storyboard](https://user-images.githubusercontent.com/16518399/52604128-c6dacc00-2e50-11e9-9620-9ae692bfc1ea.png)
