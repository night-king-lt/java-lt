package designPattern.factory;

/**
 * @Author liu.teng
 * @Date 2021/7/20 15:32
 * @Version 1.0
 *   简单工厂模式（静态工厂模式） -> 创建型设计模式
 */
public class CarSimpleFactory {

    public static final String TOPTYPE = "toptype";
    public static final String MIDTYPE = "midtype";
    public static final String LOWTYPE = "lowtype";

    public static ICar create(String mark){
        ICar obj = null;
        if(mark.equals(TOPTYPE)){//如果是高档类型
            obj = new TopCar();
        }
        else if(mark.equals(MIDTYPE)){
            obj = new MidCar();
        }
        else if(mark.equals(LOWTYPE)){
            obj = new LowCar();
        }
        return obj;
    }

    public static void main(String[] args) {
        ICar obj = CarSimpleFactory.create("toptype");
    }

}
