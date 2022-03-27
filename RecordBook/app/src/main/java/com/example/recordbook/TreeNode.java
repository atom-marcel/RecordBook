package com.example.recordbook;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TreeNode {
    TreeNode left = null;
    TreeNode right = null;
    JSONObject value = null;

    public TreeNode(JSONObject obj) {
        value = obj;
    }

    public TreeNode insertOrdered(TreeNode node, JSONObject obj) {
        if(node.value == null)  {
            node.value = obj;
            return node;
        }
        try {
            if (obj.get("start").toString().compareTo(node.value.get("start").toString()) < 0) {
                if(node.left != null) {
                    insertOrdered(node.left, obj);
                } else {
                    node.left = new TreeNode(obj);
                }
            } else {
                if(node.right != null) {
                    insertOrdered(node.right, obj);
                } else {
                    node.right = new TreeNode(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());
        }
        return node;
    }

    public JSONArray getOrderedJSONArray(TreeNode node, JSONArray input) {
        if(node.value == null) return input;
        if(node.left != null) {
            getOrderedJSONArray(node.left, input);
        }
        input.put(node.value);
        if(node.right != null) {
            getOrderedJSONArray(node.right, input);
        }
        return input;
    }
}
