/**
 * @description: 用于把后缀表达式转化为二叉树的形式，然后回其中的某些运算交换位置
 * 最终目的是为了得到唯一的后缀表达式(进行重复判定)
 * @date: 2021/10/4 19:42
 * @version: 1.0
 */
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {
    TreeNode binaryTree;
    Queue<String> queue;
    private String str1 = "";
    private String str2 = "";
    private String str3 = "";

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    public String getStr3() {
        return str3;
    }

    public BinaryTree(Queue<String> queue) {
        this.binaryTree = null;
        this.queue = queue;
    }

//  将后序遍历表达式转化为二叉树
    public void creatTree() {
        Stack<TreeNode> nodeStack = new Stack<TreeNode>();
        while(!queue.isEmpty()){
            String temp = queue.poll() + "";
            // 以当前的元素的值新建一个节点
            TreeNode node = new TreeNode(temp);

            // 如果是数字
            if(isDigit(temp)){
                nodeStack.push(node);

            // 如果是操作符
            } else if(isOperator(temp)){
                //从栈里弹出两个节点作为当前节点的左右子节点
                TreeNode rightNode = nodeStack.pop();
                TreeNode leftNode = nodeStack.pop();
                node.setLeftChild(leftNode);
                node.setRightChild(rightNode);
                if(rightNode.getHeight() > leftNode.getHeight()){
                    node.setHeight(rightNode.getHeight() + 1);
                }else{
                    node.setHeight(leftNode.getHeight()+1);
                }
                // 入栈
                nodeStack.push(node);
            }
        }
        this.binaryTree = nodeStack.pop();
        this.binaryTree = balanceCheck(binaryTree);
    }

//  将转化好的二叉树 转化为 字符串
    public void traverseString(TreeNode tree){
        if (tree == null){
            return;
        }
        str1 += tree.getValue();
        traverseString(tree.getLeftChild());
        str2 += tree.getValue();
        traverseString(tree.getRightChild());
        str3 += tree.getValue();
    }

//  进行二叉树的转换，确保后序遍历输出的结果一致
    void transferTree(TreeNode current){
        transferFormat(current);

        if (current.getLeftChild() != null) {
            transferTree(current.getLeftChild());
        }
        if (current.getRightChild() != null) {
            transferTree(current.getRightChild());
        }
    }


//  转化的逻辑，确保该运算式不会重复
    void transferFormat(TreeNode unJudge){
        if("*".equals(unJudge.getValue()) || "+".equals(unJudge.getValue())){
            String left = unJudge.getLeftChild().getValue();
            String right = unJudge.getRightChild().getValue();
            if(isDigit(left) && isDigit(right)){
                if(Integer.parseInt(left) > Integer.parseInt(right)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }
            }
            if(isOperator(left) && isDigit(right)){
                TreeNode temp = unJudge.getLeftChild();
                unJudge.setLeftChild(unJudge.getRightChild());
                unJudge.setRightChild(temp);
            }
            if(isOperator(left) && isOperator(right)){
                if("+".equals(right) && "*".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }else if("+".equals(right) && "÷".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }else if("-".equals(right) && "+".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }else if("-".equals(right) && "*".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }else if("-".equals(right) && "÷".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }else if("*".equals(right) && "÷".equals(left)){
                    TreeNode temp = unJudge.getLeftChild();
                    unJudge.setLeftChild(unJudge.getRightChild());
                    unJudge.setRightChild(temp);
                }
            }
        }

    }




    /**
     * @param node  根节点
     * @return
     */
    public TreeNode balanceCheck(TreeNode node){
        int choice = 0;
        //如果左子树的高度 - 右子树的高度 2
        if ((height(node.getLeftChild()) - height(node.getRightChild()) == 2)){
            choice = treeType(node.getValue(),node.getLeftChild().getValue());
            //判断 只需要一次右旋 还是需要多次旋转
            if (isDigit(node.getLeftChild().getRightChild().getValue()) && choice == 1){
                    //右旋 插入左孩子的左子树
                node = rightRotate(node);
            }else if(!isDigit(node.getLeftChild().getRightChild().getValue()) && choice == 1){
                    //LR
                node = leftRightRatate(node);
            }
        }
            //如果右子树的高度 - 左子树的高度 为 2
        if (height(node.getRightChild()) - height(node.getLeftChild()) == 2){
            choice = treeType(node.getValue(),node.getLeftChild().getValue());
            if (isDigit(node.getRightChild().getLeftChild().getValue()) && choice == 1){
                //左旋 插入右子树的右孩子
                node = leftRotate(node);
            } else{
                //插入右子树的左孩子 先右旋 在左旋 RL
                node = rightLeftRotate(node);
            }
        }
        return node;
    }

    int treeType(String oper1, String oper2){
        if("*".equals(oper1) || "÷".equals(oper1)){
            if("*".equals(oper2)){
                return 1;
            }else if ("÷".equals(oper2)){
                return 1;
            }else return -1;
        }else if("+".equals(oper1) || "-".equals(oper2)){
            if("+".equals(oper2)){
                return 1;
            }else if ("-".equals(oper2)){
                return 1;
            }else return -1;
        }
        return 0;

    }

    /**
     * 右旋操作 新增结点插入的根节点的左子树的左孩子
     * @param node  从这个结点开始 旋转
     * @return  根节点
     */
    public TreeNode rightRotate(TreeNode node){
        //取得左孩子
        TreeNode leftNode = node.getLeftChild();
        //左孩子的右结点是根节点的左孩子
        node.setLeftChild(leftNode.getRightChild());
        //左孩子的右孩子为根节点
        leftNode.setRightChild(node);
        //重新计算结点的高度
        node.setHeight(Math.max(height(node.getLeftChild()),height(node.getRightChild())) + 1);
        //node 就是 leftnode的 右孩子 了
        leftNode.setHeight(Math.max(height(leftNode.getLeftChild()),height(leftNode.getRightChild())) + 1);
        //返回新的结点
        return leftNode;
    }

    /**
     * 左旋 新增结点插入根节点的右子树的右孩子
     * @param node 结点
     * @return  根节点
     */
    public TreeNode leftRotate(TreeNode node){
        //取得右孩子
        TreeNode rightNode = node.getRightChild();
        //右孩子的左结点变成根节点的右孩子
        node.setRightChild(rightNode.getLeftChild());
        //右孩子的左孩子为根节点
        rightNode.setLeftChild(node);
        //重新计算高度
        node.setHeight(Math.max(height(node.getLeftChild()),height(node.getRightChild())) + 1);
        //node 就变成了 rightNode的左孩子
        node.setHeight(Math.max(height(rightNode.getLeftChild()),height(rightNode.getRightChild())) + 1);
        rightNode.setHeight(node.getHeight());
        return  rightNode;
    }

    /**
     * 先左旋 在右旋
     * 左旋：找到最近的不平衡节点，对其左子树进行左旋
     * 右旋：对不平衡结点右旋
     * @return
     */
    public TreeNode leftRightRatate(TreeNode node){
        //取得左孩子
        TreeNode leftNode = node.getLeftChild();
        //左孩子的左结点是根节点的左孩子
        node.setLeftChild(leftNode.getLeftChild());
        //左孩子的右孩子为根节点
        leftNode.setLeftChild(node);
        //重新计算结点的高度
        node.setHeight(Math.max(height(node.getLeftChild()),height(node.getRightChild())) + 1);
        //node 就是 leftnode的 右孩子 了
        leftNode.setHeight(Math.max(height(leftNode.getLeftChild()),height(leftNode.getRightChild())) + 1);
        //返回新的结点
        return leftNode;
    }

    /**
     * 先右旋 在左旋
     * RL：新增结点插入根节点的右子树的左子树
     * 右旋：先找到最近的不平衡节点，对齐右子树进行右旋
     * 左旋：对不平衡节点左旋
     * @param node 不平衡结点
     * @return
     */
    public TreeNode rightLeftRotate(TreeNode node){
        TreeNode rightNode = node.getRightChild();
        node.setRightChild(rightNode.getRightChild());
        rightNode.setRightChild(node);
        node.setHeight(Math.max(height(node.getLeftChild()),height(node.getRightChild())) + 1);
        rightNode.setHeight(Math.max(height(rightNode.getLeftChild()),height(rightNode.getRightChild())) + 1);
        return rightNode;
    }


    static boolean isDigit(String unJudge) {
        for (int i = 0; i < unJudge.length(); i++) {
            if (!Character.isDigit(unJudge.charAt(i)))
                return false;
        }
        return true;
    }

    static boolean isOperator(String unJudge) {
        return "+".equals(unJudge) || "-".equals(unJudge) || "*".equals(unJudge) || "÷".equals(unJudge);
    }




    /**
     * 得到我们当前结点的高度
     * @param node
     * @return
     */
    public int height(TreeNode node){
        return node == null ? -1 : node.getHeight();
    }

    public void preTraverse(){
        preTraverse(binaryTree);
    }

    public void preTraverse(TreeNode node){
        if (node != null){
            System.out.print(node.getValue() + " ");
            //访问左子树
            preTraverse(node.getLeftChild());
            preTraverse(node.getRightChild());
        }
    }




}

class TreeNode {
    private TreeNode leftChild;
    private TreeNode rightChild;
    private String value;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TreeNode(String value) {
        this.value = value;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}





