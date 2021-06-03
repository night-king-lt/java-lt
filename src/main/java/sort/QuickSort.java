package sort;

public class QuickSort {
    public static void main(String[] args) {
        String s = "2,3,5,11,1064,1425,1791,1792,2054,4061,6172,7308,7309,7426,7463,9351,9762,9949,13767,18964,25187,25194,25196,25203,25204,25205,25733,25734,25778,27323,27961,27962,29805,29806,29807,29808,29809,29810,29811,29812,29813,30179,30346,30543,31504,32942,32943,20,21,22,23,28,1423,1424,1426,6173,9352,13271,13272,13273,13274,13768,18667,18668,18822,18823,18824,29620,29621,29622,29623,29624,29625,29626,29627,29628,32944,40830,40835,52592,40810,43688,43715,43718,51931,43716,29620,29621,29623,29627,56780";
        String[] strings = s.split(",");
        int[] result = stringToInt(strings);
//        for (int next: result){
//            System.out.print(next + " ");
//        }
        qSort(result, 0, result.length - 1);
        for (int next: result){
            System.out.print(next + ",");
        }
    }

    public static int[] stringToInt(String[] strings){
        int[] result = new int[strings.length];
        for (int i=0; i< strings.length; i++ ){
            try{
                result[i] = Integer.parseInt(strings[i]);
            }catch (Exception e){
                System.out.println(strings[i] + " : " +i );
            }

        }
        return result;
    }

    public static void qSort(int[] n, int low, int hight){
        if ( low >= hight){
            return ;
        }
        int i, j, t, tmp;
        i = low ;
        j = hight;
        t = n[low]; // 定义一个基准数
        while ( i < j ){
            while( t <= n[j] && i < j){  // j从右往左找比基准数小的
                j--;
            }
            while( t >= n[i] && i < j){  // i从左往右找比基准数大的
                i++;
            }
            if( i < j){  // 如果i 依旧小于 j   交换i，j下标对应的元素
                tmp = n[j];
                n[j] = n[i];
                n[i] = tmp;
            }
        }
        n[low] = n[i]; // 基准数归位
        n[i] = t;
        qSort(n, low, i);
        qSort(n, i+1, hight);

    }
}
