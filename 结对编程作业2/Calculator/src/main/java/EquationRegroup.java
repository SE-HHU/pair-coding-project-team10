import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @Description
 */
public class EquationRegroup {
    /**
     * Description:
     *  传入一个字符串表达式, 生成一个队列形式的后缀表达式,
     队列里面是字符串形式的 数字和运算式
     * @param:[str] 传入四则运算表达式
     * @return: 返回一个队列
     */
    public static Queue<String> transferQueue(String str){
        //用一个栈来进行 中缀表达式 向 后缀表达式 转化
        Stack<Character> stackOper = new Stack<Character>();
        Queue<String> queue = new LinkedList<String>();
        //用字符串数组存储表达式里面的每一个数字
        char[] unDeal = str.toCharArray();
        //遍历表达式,数字直接存入队列,符号放在栈里面,根据栈的先进后出原则,决定运算顺序
        for(int i = 0; i < unDeal.length;){
            if(unDeal[i] >= '0' && unDeal[i] <= '9' || unDeal[i]=='/'){
                StringBuilder operand = new StringBuilder();
                boolean judge = i < unDeal.length && (unDeal[i] >= '0' && unDeal[i] <= '9' || unDeal[i] == '/');
                //特别要注意 i < unDeal.length 这个条件, 这是为了防止最后是数字的时候导致溢出
                //字符串按位读取, 形成一个整的字符串存入队列
                while(judge){
                    operand.append(unDeal[i]);
                    i++;
                    judge = i < unDeal.length && (unDeal[i] >= '0' && unDeal[i] <= '9' || unDeal[i] == '/');
                }
                queue.add(operand.toString());
            }else if(unDeal[i] == ')'){
                while(!stackOper.isEmpty() && stackOper.peek() != '('){
                    queue.add(stackOper.pop() + "");
                }
                stackOper.pop();
                i++;
            }else{
                while(!stackOper.isEmpty() && compare(stackOper.peek(), unDeal[i]) < 0){
                    queue.add(stackOper.pop() + "");
                }
                stackOper.add(unDeal[i]);
                i++;
            }
        }
        //当执行完上述操作以后，栈不为空，则将栈中的元素一一赋值给队列
        while(!stackOper.isEmpty()){
            queue.add(stackOper.pop() + "");
        }
        return queue;
    }

    private static int compare(char op1, char op2){
        if(op1 == '(' || op2 == '(')  return 1;
        if(op2 == '+' || op2 == '-')  return -1;
        if(op2 == '×' && (op1 == '×' || op1 == '÷')) return 1;
        if(op2 == '÷' && (op1 == '×' || op1 == '÷')) return 1;
        return 1;
    }

}

