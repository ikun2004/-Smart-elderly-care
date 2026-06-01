package cn.yangeit.controller.admin;


import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.AliOSSUtils;
import cn.yangeit.common.PDFUtil;
import cn.yangeit.config.AIModelInvoker;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.HealthAssessmentDto;
import cn.yangeit.mapper.HealthAssessmentMapper;
import cn.yangeit.pojo.HealthAssessment;
import cn.yangeit.vo.admin.HealthReportVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 健康评估Controller
 */
@RestController
@RequestMapping("/admin/healthAssessment")
@Tag(name = "管理端-健康评估模块", description = "健康评估管理")
@Slf4j //日志注解
public class HealthAssessmentController {
    @Autowired
    HealthAssessmentMapper healthAssessmentMapper;
    @Autowired
    AliOSSUtils aliOSSUtils;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    AIModelInvoker aiModelInvoker;
    @PostMapping("/upload")
    @Operation(description = "上传体检报告为pdf接口")
    public AjaxResult uploadFile
            (@Parameter(description = "上传的文件", required = true) @RequestBody MultipartFile file,
             @Parameter(description = "身份证号码", required = true) String idCard
            ) throws Exception {
        log.info("上传源文件文件名字：{} 身份证号码:{}",file.getOriginalFilename(),idCard);
        //调用阿里云OSS工具类，将上传上来的文件存入阿里云
            String url = aliOSSUtils.upload(file);
            //读取pdf文件内容
            String content = PDFUtil.pdfToString(file.getInputStream());
            //把内容存储到redis中，key为idCardNo,value:pdf文件内容
            redisTemplate.opsForHash().put("healthReport",idCard, content);
            //设置过期时间5分钟,ai识别后销毁
            redisTemplate.expire("healthReport",5, TimeUnit.MINUTES);
            return AjaxResult.success("上传成功",url);}

    @Operation(description = "新增健康评估")
    @PostMapping
    public AjaxResult add(@Parameter(description = "健康评估实体")
                          @RequestBody HealthAssessmentDto healthAssessmentDto)
    {
        try {
            //1.组装prompt(基础的模板+redis中存储的体检报告的内容)
            String prompt = getPrompt(healthAssessmentDto);
            //2.调用百炼大模型来分析数据
            String result = aiModelInvoker.tyInvoker(prompt);
            //3.解析数据   json转换为对象
            HealthReportVo healthReportVo = JSONUtil.toBean(result, HealthReportVo.class);
            //4.存储到数据库中
            Long id = saveHealthAssessment(healthReportVo,healthAssessmentDto);
            //5.获取ID返回
        } catch (Exception e) {
            throw new BaseException("AI分析失败");
        }
        return AjaxResult.success();
    }
    //将pdf内容集合提示词整合在一起，整除一套提示词，提交AI大模型
    private String getPrompt(HealthAssessmentDto healthAssessmentDto) {

        //从redis中获取内容
        String content = (String) redisTemplate.opsForHash().get("healthReport", healthAssessmentDto.getIdCard());
        if(StrUtil.isEmpty(content)){
            throw new BaseException("文件内容不存在，请重新上传");
        }

        String prompt = "请以一个专业医生的视角来分析这份体检报告，报告中包含了一些异常数据，我需要您对这些数据进行解读，并给出相应的健康建议。\n" +
                "体检内容如下：\n" +
                content+"\n" +
                "\n" +
                "要求：\n" +
                "1. 提取体检报告中的“总检日期”；\n" +
                "2. 通过临床医学、疾病风险评估模型和数据智能分析，给该用户的风险等级和健康指数给出结果。风险等级分为(使用中文）：健康、提示、风险、危险、严重危险。健康指数范围为0至100分；\n" +
                "3. 根据用户身体各项指标数据，详细说明该用户各项风险等级的占比是多少，最多保留两位小数。结论格式：该用户健康占比20.00%，提示占比20.00%，风险占比20%，危险占比20%，严重危险占比20%；\n" +
                "4. 对于体检报告有异常数据，请列出（异常数据的结论、体检项目名称、检查结果、参考值、单位、异常解读、建议）这8字段。解读异常数据，解决这些数据可能代表的健康问题或风险。分析可能的原因，包括但不限于生活习惯、饮食习惯、遗传因素等。基于这些异常数据和可能的原因，请给出具体的健康建议，包括饮食调整、运动建议、生活方式改变以及是否需要进一步检查或治疗等。\n" +
                "结论格式：异常数据的结论：肥胖，体检项目名称：体重指数BMI，检查结果：29.2，参考值>24，单位：-。异常解读：体重超标包括超重与肥胖。体重指数（BMI）=体重（kg）/身⾼（m）的平⽅，BMI≥24为超重，BMI≥28为肥胖；男性腰围≥90cm和⼥性腰围≥85cm为腹型肥胖。体重超标是⼀种由多因素（如遗传、进⻝油脂较多、运动少、疾病等）引起的慢性代谢性疾病，尤其是肥胖，已经被世界卫⽣组织列为导致疾病负担的⼗⼤危险因素之⼀。AI建议：采取综合措施预防和控制体重，积极改变⽣活⽅式，宜低脂、低糖、⾼纤维素膳⻝，多⻝果蔬及菌藻类⻝物，增加有氧运动。若有相关疾病（如⾎脂异常、⾼⾎压、糖尿病等）应积极治疗。\n" +
                "5. 根据这个体检报告的内容，分别是给人体的8大系统打分，每项满分为100分，8大系统分别为：呼吸系统、消化系统、内分泌系统、免疫系统、循环系统、泌尿系统、运动系统、感官系统\n" +
                "6. 给体检报告做一个总结，总结格式：体检报告中尿蛋⽩、癌胚抗原、⾎沉、空腹⾎糖、总胆固醇、⽢油三酯、低密度脂蛋⽩胆固醇、⾎清载脂蛋⽩B、动脉硬化指数、⽩细胞、平均红细胞体积、平均⾎红蛋⽩共12项指标提示异常，尿液常规共1项指标处于临界值，⾎脂、⾎液常规、尿液常规、糖类抗原、⾎清酶类等共43项指标提示正常，综合这些临床指标和数据分析：肾脏、肝胆、⼼脑⾎管存在隐患，其中⼼脑⾎管有“⾼危”⻛险；肾脏部位有“中危”⻛险；肝胆部位有“低危”⻛险。\n" +
                "\n" +
                "输出要求：\n" +
                "最后，将以上结果输出为JSON格式，不要包含其他的文字说明，所有的返回结果都是json，详细格式如下：\n" +
                "\n" +
                "{\n" +
                "  \"totalCheckDate\": \"YYYY-MM-DD\",\n" +
                "  \"healthAssessment\": {\n" +
                "    \"riskLevel\": \"healthy/caution/risk/danger/severeDanger\",\n" +
                "    \"healthIndex\": XX.XX\n" +
                "  },\n" +
                "  \"riskDistribution\": {\n" +
                "    \"healthy\": XX.XX,\n" +
                "    \"caution\": XX.XX,\n" +
                "    \"risk\": XX.XX,\n" +
                "    \"danger\": XX.XX,\n" +
                "    \"severeDanger\": XX.XX\n" +
                "  },\n" +
                "  \"abnormalData\": [\n" +
                "    {\n" +
                "      \"conclusion\": \"异常数据的结论\",\n" +
                "      \"examinationItem\": \"体检项目名称\",\n" +
                "      \"result\": \"检查结果\",\n" +
                "      \"referenceValue\": \"参考值\",\n" +
                "      \"unit\": \"单位\",\n" +
                "      \"interpret\":\"对于异常的结论进一步详细的说明\",\n" +
                "      \"advice\":\"针对于这一项的异常，给出一些健康的建议\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"systemScore\": {\n" +
                "    \"breathingSystem\": XX,\n" +
                "    \"digestiveSystem\": XX,\n" +
                "    \"endocrineSystem\": XX,\n" +
                "    \"immuneSystem\": XX,\n" +
                "    \"circulatorySystem\": XX,\n" +
                "    \"urinarySystem\": XX,\n" +
                "    \"motionSystem\": XX,\n" +
                "    \"senseSystem\": XX\n" +
                "  },\n" +
                "  \"summarize\": \"体检报告的总结\"\n" +
                "}";
        return prompt;
    }
    //调用saveHealthAssessment存储数据
    private Long saveHealthAssessment(HealthReportVo healthReportVo, HealthAssessmentDto healthAssessmentDto) {
        //1.创建HealthAssessment对象
        HealthAssessment healthAssessment = new HealthAssessment();
        healthAssessment.setAbnormalAnalysis(JSONUtil.toJsonStr(healthReportVo.getAbnormalData()));
        healthAssessment.setAdmissionStatus(1);
        //2.通过身份证号获取数据
        String idCard = healthAssessmentDto.getIdCard();
        healthAssessment.setIdCard(idCard);
        healthAssessment.setAge(IdcardUtil.getAgeByIdCard(idCard));
        healthAssessment.setGender(IdcardUtil.getGenderByIdCard(idCard));
        healthAssessment.setBirthDate(IdcardUtil.getBirthByIdCard(idCard));
        healthAssessment.setAssessmentTime(LocalDateTime.now());
        healthAssessment.setDiseaseRisk(JSONUtil.toJsonStr(healthReportVo.getRiskDistribution()));
        healthAssessment.setElderName(healthAssessmentDto.getElderName());

        //3.老人的健康分值
        double healthScore = healthReportVo.getHealthAssessment().getHealthIndex();
        healthAssessment.setHealthScore(String.valueOf(healthScore));
        //小于60以下，不推荐入住，没有护理等级，大于等于60  特级  >= 70  一级     >=80  二级   >=90  三级
        String nursingLevelName = getNursingLevelName(healthScore);
        healthAssessment.setNursingLevelName(nursingLevelName);
        healthAssessment.setPhysicalExamInstitution(healthAssessmentDto.getPhysicalExamInstitution());
        healthAssessment.setPhysicalReportUrl(healthAssessmentDto.getPhysicalReportUrl());
        healthAssessment.setReportSummary(healthReportVo.getSummarize());
        healthAssessment.setRiskLevel(healthReportVo.getHealthAssessment().getRiskLevel());
        healthAssessment.setSuggestionForAdmission(getSuggestionForAdmission(healthScore));
        healthAssessment.setSystemScore(JSONUtil.toJsonStr(healthReportVo.getSystemScore()));
        healthAssessment.setTotalCheckDate(healthReportVo.getTotalCheckDate());
        healthAssessment.setCreateTime(LocalDateTime.now());
        healthAssessment.setUpdateTime(LocalDateTime.now());
        healthAssessment.setCreateBy("admin");
        healthAssessment.setUpdateBy("admin");

        //调用数据库保存
        healthAssessmentMapper.insert(healthAssessment);
        //结果返回
        return healthAssessment.getId();
    }

    /**
     * 获取护理等级
     * @param healthScore
     * @return
     */
    private String getNursingLevelName(double healthScore) {

        //判断参数的有效性
        if(healthScore < 0 || healthScore > 100){
            throw new IllegalArgumentException("健康评分必须在0到100之间");
        }

        if(healthScore >= 90){
            return "三级护理等级";
        }else if (healthScore >= 80){
            return "二级护理等级";
        }else if (healthScore >= 70){
            return "一级护理等级";
        }else if (healthScore >= 60){
            return "特级护理等级";
        }else {
            return "无";
        }
    }

    /**
     * 获取入住建议
     * @param healthScore
     * @return
     */
    private Integer getSuggestionForAdmission(double healthScore) {
        if(healthScore >= 60){
            return 0;
        }
        return 1;
    }

    /**
     * 查询健康评估列表
     */
    @Operation(description = "查询健康评估列表")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody AdminPageDTO dto) {
        Page<HealthAssessment> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<HealthAssessment> wrapper = new QueryWrapper<>();
        // 模糊匹配 name id_card_no phone
        wrapper.like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "elder_name", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "id_card", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "disease_risk", dto.getSearchKey());
        wrapper.orderByDesc("create_time");
        Page<HealthAssessment> result = healthAssessmentMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }



    /**
     * 获取健康评估详细信息
     */
    @Operation(description = "获取健康评估详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@Parameter(description = "健康评估ID", required = true)
                              @PathVariable("id") Long id) {
        HealthAssessment healthAssessment = healthAssessmentMapper.selectById(id);
        return AjaxResult.success(healthAssessment);
    }


    /**
     * 修改健康评估
     */
    @Operation(description = "修改健康评估")
    @PutMapping
    public AjaxResult edit(@Parameter(description = "健康评估实体")
                           @RequestBody HealthAssessment healthAssessment) {
        healthAssessment.setUpdateTime(LocalDateTime.now());
        healthAssessmentMapper.updateById(healthAssessment);
        return AjaxResult.success();
    }

    /**
     * 删除健康评估
     */
    @Operation(description = "删除健康评估")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        healthAssessmentMapper.deleteById(id);
        return AjaxResult.success();
    }



}
