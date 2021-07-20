package nowCode;

import java.util.ArrayList;

/**
 * @Author liu.teng
 * @Date 2021/7/6 20:03
 * @Version 1.0
 *
 *  描述
 * 给定一个二叉树，返回该二叉树层序遍历的结果，（从左到右，一层一层地遍历）
 */
public class NC15 {

    ArrayList<ArrayList<Integer>> res = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> levelOrder (TreeNode root) {
        if (root == null){
            return res;
        }
        count(root, 0);
        return res;
    }

    public void count(TreeNode node, int level){
        if (level == res.size()){
            res.add(new ArrayList<>());
        }
        ArrayList<Integer> tmp = res.get(level);
        tmp.add(node.val);
        if (node.left != null){
            count(node.left, level + 1);
        }

        if (node.right != null){
            count(node.right, level + 1);
        }
    }


    public static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;
    }

}
