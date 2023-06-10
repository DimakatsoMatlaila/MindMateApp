package com.example.login;

import android.content.Intent;

import java.util.Stack;

public class PageStack {
    private static Stack<Intent> stack = new Stack<>();

    public static void push(Intent pageName) {
        stack.push(pageName);
    }

    public static Intent pop() {
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return null;
    }

    public static Intent peek() {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return null;
    }

    public static boolean isEmpty() {
        return stack.isEmpty();
    }
}
