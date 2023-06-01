package com.example.login;

import java.util.Stack;

public class PageStack {
    private static Stack<String> stack = new Stack<>();

    public static void push(String pageName) {
        stack.push(pageName);
    }

    public static String pop() {
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return null;
    }

    public static String peek() {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return null;
    }

    public static boolean isEmpty() {
        return stack.isEmpty();
    }
}
