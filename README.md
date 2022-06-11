# Player de música para android

<p>O aplicativo consiste em um player de músicas para android que reproduzirá músicas previamente inseridas. Ele exigirá que o usuário padrão faça um cadastro dando-lhe acesso a biblioteca de músicas existente.</p>

## Alunos
<p>João dos Santos Rocha</p>
<p>José Augusto Cardoso Costa</p>

## Papéis
<p>Basicamente haverão dois tipos de usuários, um deles é o administrador que terá o cadastro previamente adicionado por um desenvolvedor, este poderá adicionar novas músicas e artistas, além de modificar os já existentes, ou excluí-los. O segundo tipo será o usuário padrão, este precisará realizar um cadastro ao iniciar o aplicativo, ele poderá visualizar as músicas existentes, além de poder criar playlists usando as músicas da biblioteca.</p>

## Requisitos funcionais
### Administrador
<ul>
  <li>CRUD músicas</li>
    <p><b>Create</b>: <br>
    - Entrada: nome da música, Artista, arquivo de áudio, letra, nome do álbum, tipos musicais, duração e imagem; <br>
    - Processamento: Verifica se foi informado nome da música, artista e o arquivo de áudio pois são necessários para incluir a música; <br>
    - Retorno: sucesso ou falha.<br>
    </p>
    <p><b>Read</b>: <br>
    - Entrada: Uma string qualquer; <br>
    - Processamento: Busca dentre nome de músicas; <br>
    - Retorno: Qualquer música correspondente ou nulo (caso não encontrado)
    </p>
    <p><b>Update</b>: <br>
    - Entrada: nome da música, Artista, arquivo de áudio, letra, nome do álbum e duração; <br>
    - Processamento: Altera a música desejada com os valores informados; <br>
    - Retorno: sucesso ou falha;
    </p>
  <li>CRUD artistas</li>
    <p><b>Create</b>: <br>
    - Entrada: nome do artista, naturalidade, tipos musicais e imagem; <br>
    - Processamento: Verifica se foi informado nome do artista pois este é necessário <br>
    - Retorno: sucesso ou falha.<br>
    </p>
    <p><b>Read</b>: <br>
    - Entrada: Uma string qualquer; <br>
    - Processamento: Busca dentre nome de artistas; <br>
    - Retorno: Qualquer artista correspondente ou nulo (caso não encontrado)
    </p>
    <p><b>Update</b>: <br>
    - Entrada: nome do artista, naturalidade, tipos musicais e imagem; <br>
    - Processamento: Altera o artista desejado com os valores informados; <br>
    - Retorno: sucesso ou falha;
    </p>
</ul>

### Usuário
<ul>
  <li>Criar playlists</li>
  <li>Alterar nome de usuário</li>
  <li>Alterar foto</li>
  <li>Alterar senha</li>
  <li>Remover playlists</li>
  <li>Buscar música/artista</li>
</ul>
