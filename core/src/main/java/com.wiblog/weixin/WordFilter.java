package com.wiblog.weixin;

import com.alibaba.fastjson.JSON;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/23
 */
@Component
public class WordFilter {

    public String automaticSelection(String title) {
        //停用词
        List<Word> lists = WordSegmenter.seg(title, SegmentationAlgorithm.BidirectionalMaximumMatching);
        PartOfSpeechTagging.process(lists);
        System.out.println(JSON.toJSONString(lists));
        StringBuilder place = new StringBuilder();
        for (Word word : lists) {
            String des = word.getPartOfSpeech().getDes();
            if (!"动词".equals(des)){
                System.out.println("==" + des + "==" + word.getText());
                place.append(word.getText());
            }
        }
        return place.toString();
    }
}
