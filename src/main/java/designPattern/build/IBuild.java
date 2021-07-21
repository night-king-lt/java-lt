package designPattern.build;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 * （2）定义n个生成器Build类。
 * 根据语义，生成器是用来生成Product对象的，因此一般来说，Product是生成器类的一个成员变量；
 * 根据语义，每创建一个Product对象，本质上都需要先创建Unit1，Unit2，…， UnitN，再把它们组合成所需的Product对象，
 * 因此需要n个createUnit（）方法及一个组合方法composite()；由于createUnit()及composite()是共性，
 * 因此可定义共同的生成器类接口， n个生成器类均从此接口派生即可。
 *
 *  若需求分析发生变化，只需增加或删除相应的生成器类即可，无需修改已有的类代码。
 */
//定义生成器类接口IBuild
public interface IBuild {
    public void createUnit1();
    public void createUnit2();
    public void createUnit3();
    public Product composite();
}

//生成第一种Product
class BuildProduct implements IBuild {
    Product p = new Product();  //Product是成员变量
    @Override
    public void createUnit1() {/*p.u1 = ...... */} //创建Unit
    @Override
    public void createUnit2() {/*p.u2 = ...... */} //创建Unit2
    @Override
    public void createUnit3() {/*p.u3 = ...... */} //创建Unit3
    @Override
    public Product composite() {
    //关联Unit,Unit2,Unit3
        return p;
    }
}

//生成第2种Product
class BuildProduct2 implements IBuild {
    Product p = new Product();//Product是成员变量
    @Override
    public void createUnit1() {/*p.u1 = ...... */} //创建Unit
    @Override
    public void createUnit2() {/*p.u2 = ...... */} //创建Unit2
    @Override
    public void createUnit3() {/*p.u3 = ...... */} //创建Unit3
    @Override
    public Product composite() {
     //关联Unit1，Unit2，Unit3
        return p;
    }
}

//生成第3种Product
class BuildProduct3 implements IBuild {
    Product p = new Product();//Product是成员变量
    @Override
    public void createUnit1() {/*p.u1 = ...... */} //创建Unit
    @Override
    public void createUnit2() {/*p.u2 = ...... */} //创建Unit2
    @Override
    public void createUnit3() {/*p.u3 = ...... */} //创建Unit3
    @Override
    public Product composite() {
        //关联Unit1，Unit2，Unit3
        return p;
    }
}