# MCP

# What it is?
Method Call Parser(MCP) is a tool developed in the Java language with the purpose of analyzing projects developed in this same language.
The main objective of the tool is extract information from the project looking for breaks in confinement in classes that use the JCF framework
(Java Collection Framework). Later, the user will be shown which methods cause the containment to break and change the object's state. The tool is quite
extensible, it allow you to use your own classes for processing the method calls or even new Visitor to parse only the information of the class files you want.

# An example
We will take these two classes as an example:

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

`A` is the class that has the method that returns an object belonging to the JCF framework, the `getElements()` method, `C` is the class that has an instance of `A` and performs the call to the `getElements` method , allowing `C` to have full access to the attribute of `A` and be able to change its state, which is done in the call `a.getElements().add(new A())`, it is clear here that the break in confinement; and these are the cases that our tool seeks to identify and detail, the following is the result of the analysis of the example carried out by the tool:

`<java.util.List, add[A], boolean, C, m[], void, null>`

# How to use the tool
1. First of all you need to have maven installed, after it's installed go to this project folder and open the terminal, type `mvn clean install` This will install the necessary dependencies for the tool to work.

2. Now you need to download or clone a github project on your machine, to clone a project run the following command inside the desired folder: `git clone https://github.com/<profile>/<repository>` .

3. Copy the project root path and pass it as the value of the ```-d``` or ```-dir``` argument in the following command:
```
java -jar mcp --dir <caminho do projeto>

```
4. After running the class, a `.txt` file will be generated, which will be located in this tool's directory, with the following output:
```
<A,m(),java.util.List,C, m1(), void, mi()>
<...>
<...>
```
`A` - class that has the `m()` method;

`m()` - the method that has as a return one of the types defined in the JCF, the return used in the formulation is merely illustrative;

`java.util.List` - the fully qualified return type of the `m()` method;

`C` - class that has the `m1()` method;

`m1()` - method that has some `m()` method invocation;

`void` - the return type of the `m1()` method;

`mi` -  the invoked method that causes the confinement to be broken.
