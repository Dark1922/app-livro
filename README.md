
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
<?xml version="1.0" encoding="UTF-8"?>
<livros>
    <livro>
        <titulo>Dom Quixote</titulo>
        <autores>Miguel de Cervantes</autores>
        <dataPublicacao>1605-01-01</dataPublicacao>
        <isbn>9780060934347</isbn>
        <editora>Editora A</editora>
        <livrosSemelhantes></livrosSemelhantes>
    </livro>
    <livro>
        <titulo>1984</titulo>
        <autores>George Orwell</autores>
        <dataPublicacao>1949-06-08</dataPublicacao>
        <isbn>9780451524935</isbn>
        <editora>Editora B</editora>
        <livrosSemelhantes>Admirável Mundo Novo,Fahrenheit 451</livrosSemelhantes>
    </livro>
    <livro>
        <titulo>O Pequeno Príncipe</titulo>
        <autores>Antoine de Saint-Exupéry</autores>
        <dataPublicacao>1943-04-06</dataPublicacao>
        <isbn>9780156013925</isbn>
        <editora>Editora C</editora>
        <livrosSemelhantes>O Alquimista,O Profeta</livrosSemelhantes>
    </livro>
</livros>
```

📝 Formato TXT

```

Dom Quixote|Miguel de Cervantes|1605-01-01|9780060934347|Editora A|
1984|George Orwell|1949-06-08|9780451524935|Editora B|Admirável Mundo Novo,Fahrenheit 451

```

📊 Formato CSV

```
Título;Autores;DataPublicação;ISBN;Editora;LivrosSemelhantes

Dom Quixote;Miguel de Cervantes;1605-01-01;9780060934347;Editora A;
1984;George Orwell;1949-06-08;9780451524935;Editora B;"Admirável Mundo Novo,Fahrenheit 451"
O Pequeno Príncipe;Antoine de Saint-Exupéry;1943-04-06;9780156013925;Editora C;"O Alquimista,O Profeta"
Orgulho e Preconceito;Jane Austen;1813-01-28;9788535902775;Editora D;

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
