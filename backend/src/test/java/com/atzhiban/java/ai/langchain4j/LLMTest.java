package com.atzhida.java.ai.langchain4j;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LLMTest {
    /**
     * gpt-4o-mini璇█妯″瀷鎺ュ叆娴嬭瘯
     */
    @Test
    public void testGPTDemo() {
        //鍒濆鍖栨ā鍨?
        OpenAiChatModel model = OpenAiChatModel.builder()
                //LangChain4j鎻愪緵鐨勪唬鐞嗘湇鍔″櫒锛岃浠ｇ悊鏈嶅姟鍣ㄤ細灏嗘紨绀哄瘑閽ユ浛鎹㈡垚鐪熷疄瀵嗛挜锛?鍐嶅皢璇锋眰杞彂缁橭penAI API
                .baseUrl("http://langchain4j.dev/demo/openai/v1") //璁剧疆妯″瀷api鍦板潃锛堝鏋渁piKey = "demo"锛屽垯鍙渷鐣aseUrl鐨勯厤缃級
                .apiKey("demo") //璁剧疆妯″瀷apiKey
                .modelName("gpt-4o-mini") //璁剧疆妯″瀷鍚嶇О
                .build();
        //鍚戞ā鍨嬫彁闂?
        String answer = model.chat("浣犲ソ");
        //杈撳嚭缁撴灉
        System.out.println(answer);
    }



    /**
     * 鏁村悎SpringBoot
     */
    @Autowired
    private OpenAiChatModel openAiChatModel;
    //private ChatLanguageModel chatLanguageModel;
    @Test
    public void testSpringBoot() {
        //鍚戞ā鍨嬫彁闂?
        String answer = openAiChatModel.chat("浣犳槸璋侊紵");
        //杈撳嚭缁撴灉
        System.out.println(answer);
    }



    /**
     * ollama娴嬭瘯
     */
    @Autowired
    private OllamaChatModel ollamaChatModel;
    @Test
    public void testOllama() {
        //鍚戞ā鍨嬫彁闂?
        String answer = ollamaChatModel.chat("浣犳槸璋侊紵");
        //杈撳嚭缁撴灉
        System.out.println(answer);
    }



    /**
     * 閫氫箟鍗冮棶澶фā鍨?
     */
    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testDashScopeQwen() {
        //鍚戞ā鍨嬫彁闂?
        String answer = qwenChatModel.chat("浣犳槸璋侊紵");
        //杈撳嚭缁撴灉
        System.out.println(answer);
    }



    /**
     * 閫氫箟涓囪薄
     */
    @Test
    public void testDashScopeWanx(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();
        Response<Image> response = wanxImageModel.generate("濂囧够妫灄绮剧伒锛氬湪涓€鐗囧讥婕潃杞绘煍钖勯浘鐨?鍙よ€佹．鏋楁繁澶勶紝闃冲厜閫忚繃鑼傚瘑鏋濆彾娲掍笅閲戣壊鍏夋枒銆備竴浣嶈韩鏉愬▏灏忋€侀暱鐫€閫忔槑钖勭考鐨勭簿鐏靛皯濂崇珯鍦ㄤ竴鏈电澶х殑铇戣弴涓娿€傚ス 鏈夌潃娴疯椈鑸殑缁胯壊闀垮彂锛屽彂闂寸偣缂€鐫€钃濊壊鐨勫皬鑺憋紝鐨偆娉涚潃鐝嶇彔鑸殑寰厜銆傝韩涓婄┛鐫€鐢辩繝缁挎爲鍙跺拰鐧借壊钘よ敁缂栫粐鑰屾垚鐨?杩炶。瑁欙紝鎵嬩腑鎹х潃涓€棰楁暎鍙戠潃鏌斿拰鍏夎姃鐨勬按鏅剁悆锛屽懆鍥寸幆缁曠潃浜斿僵鏂戞枔鐨勮澊铦讹紝鑴氫笅鏄摵婊¤嫈钘撶殑鍦伴潰锛岃槕鑿囧拰钑ㄧ被妞?鐗╀笡鐢燂紝钀ラ€犲嚭绁炵鑰屾ⅵ骞荤殑姘涘洿銆?);
                System.out.println(response.content().url());
    }

}

