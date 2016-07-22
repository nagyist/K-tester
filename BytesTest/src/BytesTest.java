/*
 * CREATED ON: 2016/07/21

 * AUTHOR: Eduard Dedu 
 * eduardcdedu@gmail.com
 */
import java.io.*; 
class Parent {
    void f() {
        System.out.println("Parent");
    }
}
class Child extends Parent {
    @Override void f() {
        System.out.println("Child");
    }
}
public class BytesTest {
  public static void main(String [] args) throws IOException {
      /*
      File f1 = new File("/Users/eduarddedu/Downloads/bytes01.btf");
      File f2 = new File("/Users/eduarddedu/Downloads/bytes02.btf");
      FileInputStream fis = new FileInputStream(f2);
      int x; 
      while ( (x = fis.read()) != -1)
          System.out.print( (char) x);
          */
      Parent c = new Child();
      c.f();
  }
}
