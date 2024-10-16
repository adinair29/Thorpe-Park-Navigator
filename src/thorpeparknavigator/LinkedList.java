/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author aditya nair
 */
public class LinkedList<E> {

    static class Node<E> {

        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    Node<E> head = new Node<E>(null);
    Node<E> tail = head;
    int size;

    void add(E value) {
        tail = tail.next = new Node<E>(value);
        size++;
    }

    E get(int index) {
        if (index < 0 || size <= index) {
            System.out.println("Out of bounds return null");
            return null;
        }
        Node<E> node = head.next;
        while (index > 0) {
            node = node.next;
            index--;
        }
        return node.value;
    }

    public ArrayList<E> items() {
        ArrayList<E> arrayList = new ArrayList();
        int counter = 0;
        while (counter < size) {
            arrayList.add(this.get(counter));
            counter += 1;
        }
        Collections.reverse(arrayList);
        return arrayList;
    }
}
