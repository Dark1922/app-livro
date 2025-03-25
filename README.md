
<div align="center">
  <h3 align="center">🚀 # App Livro </h3>
 </div>


<h3 align="center"> Demonstração do projeto</h3>


![image](https://github.com/user-attachments/assets/24fe426c-9fad-4a45-bdca-d5a3d1000773)


![image](https://github.com/user-attachments/assets/76526cad-ddc8-41fa-97fb-2f79731c337d)


![image](https://github.com/user-attachments/assets/d676250b-50cd-43f8-8d6c-676995c2cd86)

<hr />

## 🔍 ISBNs Válidos para Teste

9780439708180,
9780060934347,
9788595084742,
9788576572008,
9788576573135

<hr />


## 📂 Exemplos de Arquivos de Importação

### 📄 Formato XML
```xml
<livros>
  <livro>
    <isbn>9788535914849</isbn>
    <titulo>1984</titulo>
    <autor>George Orwell</autor>
    <editora>Companhia das Letras</editora>
    <anoPublicacao>1949</anoPublicacao>
  </livro>
  <livro>
    <isbn>9788595086357</isbn>
    <titulo>O Hobbit</titulo>
    <autor>J.R.R. Tolkien</autor>
    <editora>HarperCollins</editora>
    <anoPublicacao>1937</anoPublicacao>
  </livro>
</livros>
```

📝 Formato TXT

```
ISBN|Título|Autor|Editora|Ano
9788535914849|1984|George Orwell|Companhia das Letras|1949
9788595086357|O Hobbit|J.R.R. Tolkien|HarperCollins|1937

```

📊 Formato CSV

```
isbn,titulo,autor,editora,anoPublicacao
9788535914849,1984,George Orwell,Companhia das Letras,1949
9788595086357,O Hobbit,J.R.R. Tolkien,HarperCollins,1937

```

<hr /> 

## :heavy_check_mark: Tecnologias <a name="technologies"></a>

<dl>
<dt><strong>Postgres</strong></dt>
<dt><strong>JDK 8</strong></dt>
<dt><strong>Swing</strong></dt>
<dt><strong>Hibernate</strong></dt>
<dt><strong>JPA</strong></dt>
<dt><strong>Mockito</strong></dt>
<dt><strong>Junit</strong></dt>
</dl>
 </div>
  <hr /> 


 Build do projeto

```bash
# Clonar repositório
git clone https://github.com/Dark1922/app-livro.git
cd app-livro

# Compilar e executar (Windows)
mvnw.cmd compile exec:java -Dexec.mainClass="ui.MenuPrincipal"

# Linux/Mac
./mvnw compile exec:java -Dexec.mainClass="ui.MenuPrincipal"

verificar dados do banco de dados se conferem no arquivo persistence.xml, e rodar a classe MenuPrincipal.

```
