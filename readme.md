## Endpoints

### Produto

| Method | Endpoint          | Descricao                  |
| ------ | ----------------- | -------------------------- |
| GET    | /api/produtos     | Lista todos os produtos    | -> Pagina��o
| GET    | /api/produtos/:id | Retorna um produto produto |
| POST   | /api/produtos     | Cria um produto            |
| PUT    | /api/produtos     | Edita um produto           |
| DELETE | /api/produtos/:id | Deleta um produto          |


### Categoria

| Method | Endpoint                  | Descricao                  |
| ------ | ------------------------- | -------------------------- |
| GET    | /api/categorias           | Lista todos os categorias  | -> Pagina��o
| GET    | /api/categorias/descricao | Retorna um produto produto |
| POST   | /api/categorias           | Cria um produto            |
| DELETE | /api/categorias/descricao | Deleta um produto          |




Fiquei na d�vida sobre quando usar o Spring Data Rest.
Devo usar os endpoints que ele cria automaticamente ou seria melhor
codar esses endpoits? 

Reposit�rio e DAOs, eu escolho um desses padr�es pra usar? Uso misturado?
Quando devo usar um ou outro? Eu nao compreendi bem a diferen�a entre eles.

Exceptions. Qual seria a melhor pr�tica, criar uma classe mais generica 
tipo NotFound ou criar algo mais especifico tipo ProdutoNotFound e CategoriaNotFound?
