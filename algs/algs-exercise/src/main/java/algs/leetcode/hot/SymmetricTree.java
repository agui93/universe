package algs.leetcode.hot;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode-cn.com/problems/symmetric-tree/
 * 考察:递归
 */
public class SymmetricTree {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }


    //递归的解法
    public boolean isSymmetricByRecursion(TreeNode p1, TreeNode p2) {
        if (p1 == null && p2 == null) {
            return true;
        }
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.val == p2.val
                && isSymmetricByRecursion(p1.left, p2.right)
                && isSymmetricByRecursion(p1.right, p2.left);
    }


    //BFS
    public boolean isSymmetricByBFS(TreeNode root) {
        if (root == null) {
            return true;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root.left);
        queue.add(root.right);
        while (!queue.isEmpty()) {
            TreeNode t1 = queue.poll();
            TreeNode t2 = queue.poll();
            if (t1 == null && t2 == null) {
                return true;
            }
            if (t1 == null || t2 == null) {
                return false;
            }
            if (t1.val != t2.val) {
                return false;
            }

            queue.add(t1.left);
            queue.add(t2.right);
            queue.add(t1.right);
            queue.add(t2.left);
        }

        return true;
    }

    //todo 栈模拟递归
    public boolean isSymmetricByStack() {
        return false;
    }

    public boolean isSymmetric(TreeNode root) {
//        return isSymmetricByRecursion(root, root);
        return isSymmetricByBFS(root);
    }


    public static void main(String[] args) {
        //todo 模拟二叉树的输入和输出
    }

}
