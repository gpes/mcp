README
------

Kunststoff Look&Feel 1.1.1

Developed by INCORS GmbH, published under the GNU Lesser General Public Licence.

Contributors:

- Karsten Lentzsch
- Eric Georges
- Jens Niemeyer
- Jamie LaScolea
- Julien Ponge
- Taoufik Romdhane


INSTALLATION
------------

To use the Kunststoff Look&Feel with your Swing application, you first need to put the kunststoff.jar file into the classpath of your application.

Then include this line into your application:

UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());


If you are using JavaWebStart you will have to use these two lines instead, where MyClass is the class that includes the lines:

UIManager.put("ClassLoader", MyClass.class.getClassLoader());
UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());