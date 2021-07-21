package designPattern.factory;

/**
 * @Author liu.teng
 * @Date 2021/7/20 15:38
 * @Version 1.0
 *
 *    抽象工厂设计模式 -> 创建型设计模式
 */
public interface AbstractFactory {
    public abstract ICar createCar(); //产生小汽车对象
}

//定义高档小汽车工厂
class TopFactory implements AbstractFactory {
    @Override
    public ICar createCar() {
        return new TopCar();
    }
}
//定义中档小汽车工厂
class MidFactory  implements AbstractFactory {
    @Override
    public ICar createCar() {
        return new MidCar();
    }
}
//定义低档小汽车工厂
class LowFactory implements AbstractFactory {
    @Override
    public ICar createCar() {
        return new LowCar();
    }
}
