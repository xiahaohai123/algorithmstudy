package lru;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PalindromeJudgeLinkedList {

    private Node headNode;

    public PalindromeJudgeLinkedList(String input) {
        headNode = new Node();
        initList(input);
    }

    private void initList(String input) {
        char[] chars = input.toCharArray();
        Node currentNode = headNode;
        for (char c : chars) {
            currentNode.setNext(new Node(c));
            currentNode = currentNode.getNext();
        }
    }

    /**
     * 快慢指针找到中心点
     * @return 如果是回文字符串则返回true，否则返回false
     */
    public boolean isPalindrome() {
        if (headNode.getNext() == null) {
            return false;
        }
        if (headNode.getNext().getNext() == null) {
            return true;
        }

        Node ptr0 = headNode.getNext();
        Node ptr1 = headNode.getNext().getNext();

        while (ptr1.getNext() != null && ptr1.getNext().getNext() != null) {
            ptr0 = ptr0.getNext();
            ptr1 = ptr1.getNext().getNext();
        }

        Node rightHead;
        if (ptr1.getNext() != null) {
            // 奇数
            rightHead = ptr0.getNext().getNext();
        } else {
            rightHead = ptr0.getNext();
            // 偶数
        }
        reverseList(ptr0);
        Node leftHead = ptr0;
        return isListEqual(leftHead, rightHead);
    }

    /**
     * 反转单链表方向
     * @param target 翻转尽头节点
     */
    private void reverseList(Node target) {
        Node currentNode = headNode.getNext();
        // 不用翻转
        if (currentNode == target) {
            currentNode.setNext(null);
            return;
        }
        Node lastNode = currentNode;
        currentNode = currentNode.getNext();
        lastNode.setNext(null);
        while (currentNode != target) {
            Node nextNode = currentNode.getNext();
            currentNode.setNext(lastNode);
            lastNode = currentNode;
            currentNode = nextNode;
        }
        currentNode.setNext(lastNode);
    }

    private boolean isListEqual(Node leftHead, Node rightHead) {
        Node leftNode = leftHead;
        Node rightNode = rightHead;
        while (rightNode != null) {
            if (rightNode.getC() != leftNode.getC()) {
                return false;
            }
            rightNode = rightNode.getNext();
            leftNode = leftNode.getNext();
        }
        return leftNode == null;
    }

    class Node {
        private char c;
        private Node next;

        public Node() {
        }

        public Node(char c) {
            this.c = c;
        }

        public Node(char c, Node next) {
            this.c = c;
            this.next = next;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public char getC() {
            return c;
        }
    }

    public static void main(String[] args) {
        List<String> testList = new ArrayList<>();
        testList.add("");
        testList.add("a");
        testList.add("aba");
        testList.add("ababa");
        testList.add("ab");
        testList.add("abccba");
        testList.add("aabccba");
        for (String s : testList) {
            System.out.print(s.concat(": "));
            System.out.println(new PalindromeJudgeLinkedList(s).isPalindrome());
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            PalindromeJudgeLinkedList list = new PalindromeJudgeLinkedList(scanner.nextLine());
            System.out.println(list.isPalindrome());
        }
    }
}
