package skill;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/13
 */
public class forTest {
    public static void main(String[] args) {
        for (int i=0; i < 10; i++){
            for (int j=0; j< 3; j++){
                if (j == 1){
                    break;
                }
                System.out.println(" 内层循环 j :" + j );
            }
//            System.out.println(" 外层循环 i :" + i );
        }
    }
}
