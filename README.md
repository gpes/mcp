# method-call-parser(MCP)

# What is it?
MCP is a tool developed in the java language with the purpose of analyzing projects developed in this same language.
The main objective of the analysis is to extract information from the project by searching for confinement breaks in classes that use the JCF(Java Collection Framework). Afterwards, the user will be shown which methods cause the confinement to break and change the object's state.

# An example
We will take this two classes as a sample:

```
// Target Class
class A{
    private List<A> elements;
    public List<A> getElements(){
        return this.elements;
    }
}
// Client Class
class C{
    private A a;
    public void m(){
        a.getElements().add(new A());
    }
}
```

`A` is the class that has the method that returns an object belonging to the JCF framework, the `getElements()` method, `C` is the class that has an instance of `A` and calls the `getElements` method , allowing `C` to have full access to the attribute of `A` and to change its state, which is done in the call `a.getElements().add(new A())`, it is clear here that the breaking of confinement; and these are the cases that our tool seeks to identify and detail, here is the result of the analysis of the example carried out by the tool:

`<java.util.List, add[A], boolean, C, m[], void, null>`

This is the raw text result produced by the tool, after the analysis, we use another module to produce a more interactive result to the user.

# How use the tool

1. First of all, you need to download or clone a project from github on your machine, to clone a project run the following command inside the desired folder: `git clone https://github.com/<profile>/<repository>`.

2. Now you need to have Java version 8 installed, and maven, after installed go to this project folder and open the terminal, type `mvn clean install` This will install the necessary dependencies for the tool to work.

3. You can see a brief explanations of the options using this command ```java -jar mcp --help```.

![img](https://i.imgur.com/uUmuRm4.png)

4. Copy the path of the Java project that you want to analyze, and pass it to the option ```-d``` ou ```-dir``` on the following command:
```
java -jar mcp --dir <project_path>

```
5. Após executar a classe, um arquivo `.txt` será gerado, e estará localizado no diretório desta ferramenta, com a seguinte saída:
```
<A,m(),java.util.List,C, m1(), void, mi()>
<...>
<...>
```
`A` - classe que possui o método `m()`;

`m()` - o método que possui como retorno algum dos tipos definidos no JCF, o retorno utlizado na formulação é meramente ilustrativo;

`java.util.List` - o tipo do retorno totalmente qualificado do método `m()`;

`C` - classe que possui o método `m1()`;

`m1()` - método que possui alguma invocação do método `m()`;

`void` - o tipo de retorno do método m1();

`mi` - o método invocado que causa a quebra do confinamento.
