package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: miaomiao
 * @Date: 2018/10/12
 * @Description:敏感词过滤
 **/
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //从SensitiveKeyword.txt文件中读取关键词
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveKeyword.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addKeyword(lineTxt.trim());
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败", e.getMessage());
        }
    }

    //增加关键词
    private void addKeyword(String lineTxt){
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++){
            Character c = lineTxt.charAt(i);
            if (isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null){
                node = new TrieNode();
                tempNode.addTrieNode(c,node);
            }
            tempNode = node;

            if (i == lineTxt.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode {
        //是不是关键词的结尾
        private boolean end = false;

        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addTrieNode(Character key, TrieNode trieNode) {
            subNodes.put(key, trieNode);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    //判断该字符是否为空格 * 等其他非中文或字母的字符
    private boolean isSymbol(char c){
        int ic = (int)c;
        //0x2E80到0x9FFF 表示东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (ic > 0x9FFF || ic < 0x2E80);
    }

    //过滤敏感词
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return text;
        }

        StringBuilder result = new StringBuilder();
        String replacement = "***";

        //指向敏感词前缀树的指针
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;

        while (position < text.length()){
            char c = text.charAt(position);
            if (isSymbol(c)){
                if (tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);

            if (tempNode == null){
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else {
                ++position;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

//    public static void main(String[] args){
//        SensitiveService s = new SensitiveService();
//        s.addKeyword("色情");
//        s.addKeyword("赌博");
//        System.out.print(s.filter("hi 你+好+色 +情我是"));
//
//    }
}
