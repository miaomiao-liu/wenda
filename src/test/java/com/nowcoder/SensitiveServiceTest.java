package com.nowcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: miaomiao
 * @Date: 2018/10/16
 * @Description:
 **/
public class SensitiveServiceTest implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveServiceTest.class);

    @Override
    public void afterPropertiesSet() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveKeyword.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addKeyword(lineTxt.trim());
            }
            reader.close();
        } catch (Exception e) {
            logger.error("读取敏感词失败" + e.getMessage());
        }
    }

    public void addKeyword(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);

            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineTxt.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }

    }

    private class TrieNode {
        private boolean end = false;

        public Map<Character, TrieNode> subNode = new HashMap<>();

        public void addSubNode(Character key, TrieNode trieNode) {
            subNode.put(key, trieNode);
        }

        public TrieNode getSubNode(Character key) {
            return subNode.get(key);
        }

        public boolean isKeywordEnd() {
            return end;
        }

        public void setKeywordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    public String filter(String text) {

        StringBuilder result = new StringBuilder();
        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            Character c = text.charAt(position);
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }

        }
        result.append(text.substring(begin));
        return result.toString();

    }

        public static void main(String[] args){
        SensitiveServiceTest s = new SensitiveServiceTest();
        s.addKeyword("色情");
        s.addKeyword("赌博");
        System.out.print(s.filter("hi你好色情我是"));

    }
}
