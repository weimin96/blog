package com.wiblog.utils;

import com.alibaba.nls.client.protocol.asr.SpeechRecognizerListener;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.wiblog.entity.Voice;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/30
 */
@Slf4j
@Component
public class XunFeiUtil {

    private static final String APPID = "5d36d6d1";

    private static XunFeiUtil mObject;

    private static StringBuffer mResult = new StringBuffer();

    public static Map<String, Voice> voices = new HashMap<>(16);


    /**
     * 听写
     */
    private static boolean mIsEndOfSpeech = false;
    public void Recognize(File file,String id) {
        if (SpeechRecognizer.getRecognizer() == null) {
            SpeechRecognizer.createRecognizer();
        }
        mIsEndOfSpeech = false;
        RecognizePcmfileByte(file,id);
    }

    /**
     * 自动化测试注意要点 如果直接从音频文件识别，需要模拟真实的音速，防止音频队列的堵塞
     */
    public void RecognizePcmfileByte(File file,String id) {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        //写音频流时，文件是应用层已有的，不必再保存
//		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//				"./iat_test.pcm");
        recognizer.setParameter( SpeechConstant.RESULT_TYPE, "plain" );
        recognizer.startListening(getRecognizerListener(id));

        FileInputStream fis = null;
        final byte[] buffer = new byte[64*1024];
        try {
            fis = new FileInputStream(file);
            if (0 == fis.available()) {
                mResult.append("no audio avaible!");
                recognizer.cancel();
            } else {
                int lenRead = buffer.length;
                while( buffer.length==lenRead && !mIsEndOfSpeech ){
                    lenRead = fis.read( buffer );
                    recognizer.writeAudio( buffer, 0, lenRead );
                }//end of while

                recognizer.stopListening();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end of try-catch-finally

    }

    /**
     * 听写监听器
     */
    private static RecognizerListener getRecognizerListener(String myOrder) {
        RecognizerListener recListener = new RecognizerListener() {

            @Override
            public void onBeginOfSpeech() {
                log.info("onBeginOfSpeech enter");
                log.info("*************开始录音*************");
            }

            @Override
            public void onEndOfSpeech() {
                log.info("onEndOfSpeech enter");
                mIsEndOfSpeech = true;
            }

            @Override
            public void onVolumeChanged(int volume) {
                log.info("onVolumeChanged enter");
                if (volume > 0) {
                    log.info("*************音量值:" + volume + "*************");
                }

            }

            @Override
            public void onResult(RecognizerResult result, boolean islast) {
                log.info("onResult enter");
                mResult.append(result.getResultString());

                if (islast) {
                    log.info("识别结果为=:" + mResult.toString());
                    String msg =  mResult.toString();
                    mIsEndOfSpeech = true;
                    Voice voice = voices.get(myOrder);
                    voice.setMsg(msg);
                    mResult.delete(0, mResult.length());
                }
            }

            @Override
            public void onError(SpeechError error) {
                mIsEndOfSpeech = true;
                log.info("*************" + error.getErrorCode()
                        + "*************");
            }

            @Override
            public void onEvent(int eventType, int arg1, int agr2, String msg) {
                log.info("onEvent enter");
            }

        };
        return recListener;
    }



    public static void main(String[] args) {
        SpeechUtility.createUtility("appid=" + APPID);
//        Recognize(new File("E://桌面//tmp//oA1Vzo-8PMBSH9xrmPLE-C0iLo796rsIrPS8tZUC7cgfwGQH-s8xA1t-gWtPWTeG.wav"),"11");
    }

}
