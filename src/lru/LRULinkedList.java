package lru;

import java.util.Scanner;

public class LRULinkedList<T> {

    /** 头节点 */
    private Node<T> headNode;

    /** 链表长度 */
    private int length;

    /** 容量 */
    private int capacity;

    /** 默认容量大小 */
    private static final int DEFAULT_CAPACITY = 4;

    public LRULinkedList() {
        this(DEFAULT_CAPACITY);
    }

    public LRULinkedList(int capacity) {
        if (capacity <= 0) {
            this.capacity = DEFAULT_CAPACITY;
        } else {
            this.capacity = capacity;
        }

        headNode = new Node<>();
    }

    public void add(T data) {
        Node<T> preNode = findPreNode(data);

        if (preNode != null) {
            deleteNextNode(preNode);
        } else {
            if (length >= capacity) {
                deleteEnd();
            }
        }
        insertDataBegin(data);
    }

    public void printAll() {
        Node<T> currentNode = headNode;
        StringBuilder stringBuilder = new StringBuilder("[");
        while (currentNode.next() != null) {
            currentNode = currentNode.next();
            stringBuilder.append(currentNode.getData().toString()).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).append(']');
        System.out.println(stringBuilder);
    }

    private Node<T> findPreNode(T data) {
        Node<T> currentNode = headNode;
        while (currentNode.next() != null) {
            if (data.equals(currentNode.next().getData())) {
                return currentNode;
            }
            currentNode = currentNode.next();
        }
        return null;
    }

    private void deleteNextNode(Node<T> preNode) {
        preNode.setNext(preNode.next().next());
        length--;
    }

    private void insertDataBegin(T data) {
        Node<T> nextNode = headNode.next();
        headNode.setNext(new Node<>(data, nextNode));
        length++;
    }

    private void deleteEnd() {
        Node<T> currentNode = headNode;
        if (currentNode.next() == null) {
            return;
        }
        while (currentNode.next().next() != null) {
            currentNode = currentNode.next();
        }

        currentNode.setNext(null);
        length--;
    }

    /** 节点 */
    class Node<T> {
        /** 数据 */
        private T data;
        /** 下一节点指针 */
        private Node<T> next;

        public Node() {
        }

        public Node(T data) {
            this.data = data;
        }

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        Node<T> next() {
            return next;
        }

        T getData() {
            return data;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        LRULinkedList list = new LRULinkedList();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            list.add(scanner.nextLine());
            list.printAll();
        }
    }
}
