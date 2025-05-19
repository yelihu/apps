package com.example.app1.elasticsearch.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Elasticsearch服务测试类
 */
@Slf4j
@SpringBootTest
@ComponentScan(basePackages = {"com.example.app1", "com.example.common"})
@ActiveProfiles("test")
public class ElasticsearchServiceTest {

    @Autowired
    private PersonDocumentServiceImpl personDocumentService;

    private List<PersonDocument> testPersons;

    @BeforeEach
    public void setUp() {
        log.info("=================== 测试准备开始 ===================");
        
        // 强制删除索引
        if (personDocumentService.indexExists()) {
            log.info("删除已存在的person索引");
            personDocumentService.deleteIndex();
            log.info("索引删除完成");
        } else {
            log.info("person索引不存在，无需删除");
        }
        
        // 创建新索引
        log.info("创建新的person索引");
        personDocumentService.createIndex();
        log.info("索引创建完成");

        // 准备测试数据
        log.info("准备测试数据: 4条人员记录");
        testPersons = Arrays.asList(
                createPerson("张三", "EMP001", "DEP001", "研发部", "高级工程师"),
                createPerson("李四", "EMP002", "DEP001", "研发部", "产品经理"),
                createPerson("王五", "EMP003", "DEP002", "市场部", "市场专员"),
                createPerson("赵六", "EMP004", "DEP003", "人事部", "人力资源专员")
        );

        // 保存测试数据
        log.info("批量保存测试数据到ES");
        personDocumentService.saveAll(testPersons);
        
        // 等待索引刷新
        log.info("等待索引刷新 (3秒)...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("等待索引刷新时被中断", e);
            Thread.currentThread().interrupt();
        }
        log.info("索引准备完成");
        log.info("=================== 测试准备结束 ===================");
    }

    @AfterEach
    public void tearDown() {
        log.info("=================== 测试清理开始 ===================");
        // 删除测试数据
        if (testPersons != null) {
            log.info("清理测试数据: {}条记录", testPersons.size());
            int successCount = 0;
            int failCount = 0;
            
            for (PersonDocument person : testPersons) {
                try {
                    log.debug("删除文档: ID={}, 姓名={}", person.getId(), person.getName());
                    personDocumentService.deleteById(person.getId());
                    successCount++;
                } catch (Exception e) {
                    log.error("删除文档失败: ID={}, 错误: {}", person.getId(), e.getMessage());
                    failCount++;
                }
            }
            
            log.info("文档删除统计: 成功={}, 失败={}", successCount, failCount);
        } else {
            log.info("无测试数据需要清理");
        }
        log.info("=================== 测试清理结束 ===================");
    }

    @Test
    public void testSaveAndFindById() {
        log.info("开始测试: 保存和根据ID查询");
        
        // 创建测试数据
        PersonDocument person = createPerson("测试用户", "TEST001", "TEST-DEP", "测试部门", "测试职位");
        log.info("创建测试数据: {}", person);
        
        // 保存数据
        PersonDocument savedPerson = personDocumentService.save(person);
        log.info("保存成功，生成ID: {}", savedPerson.getId());
        assertNotNull(savedPerson.getId(), "保存后应该有ID");
        
        // 等待索引刷新
        try {
            log.info("等待索引刷新...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 根据ID查询
        log.info("根据ID查询: {}", savedPerson.getId());
        Optional<PersonDocument> foundPerson = personDocumentService.findById(savedPerson.getId());
        
        if (foundPerson.isPresent()) {
            log.info("查询成功: {}", foundPerson.get());
        } else {
            log.error("未找到ID为{}的文档", savedPerson.getId());
        }
        
        assertTrue(foundPerson.isPresent(), "应该能找到保存的文档");
        assertEquals("测试用户", foundPerson.get().getName(), "姓名应该匹配");
        
        // 清理
        log.info("删除测试数据: {}", savedPerson.getId());
        personDocumentService.deleteById(savedPerson.getId());
    }

    @Test
    public void testSearchByKeyword() {
        log.info("开始测试: 关键字搜索");
        
        // 等待索引完全可搜索
        try {
            log.info("等待索引就绪...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 按关键字搜索
        log.info("执行关键字搜索: '工程师'");
        List<PersonDocument> engineers = personDocumentService.searchByKeyword("工程师");
        
        if (!engineers.isEmpty()) {
            log.info("搜索到{}条包含'工程师'的记录:", engineers.size());
            for (PersonDocument doc : engineers) {
                log.info(" - {}: {} ({})", doc.getId(), doc.getName(), doc.getPosition());
            }
        } else {
            log.warn("未找到包含'工程师'的记录");
        }
        
        assertFalse(engineers.isEmpty(), "应该能找到包含'工程师'的文档");
        
        // 分页搜索
        log.info("执行分页关键字搜索: '研发'，按name_sort排序");
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name_sort").ascending());
        Page<PersonDocument> result = personDocumentService.searchByKeyword("研发", pageable);
        
        log.info("分页搜索结果: 总记录数={}, 当前页记录数={}, 总页数={}, 当前页={}",
                result.getTotalElements(), result.getNumberOfElements(),
                result.getTotalPages(), result.getNumber());
        
        if (result.hasContent()) {
            log.info("分页搜索结果内容:");
            for (PersonDocument doc : result.getContent()) {
                log.info(" - {}: {} ({})", doc.getId(), doc.getName(), doc.getDepartmentName());
            }
        }
        
        assertTrue(result.getTotalElements() > 0, "应该能找到包含'研发'的文档");
    }

    @Test
    public void testFindByField() {
        log.info("开始测试: 按字段查询");
        
        // 根据部门ID查询
        log.info("按部门ID查询: DEP001");
        List<PersonDocument> devDeptPersons = personDocumentService.findByDepartment("DEP001");
        
        log.info("研发部(DEP001)员工列表: {}人", devDeptPersons.size());
        for (PersonDocument person : devDeptPersons) {
            log.info(" - {}: {} ({})", person.getId(), person.getName(), person.getPosition());
        }
        
        assertEquals(2, devDeptPersons.size(), "研发部应该有2名员工");
        
        // 根据员工ID查询
        log.info("按员工ID查询: EMP003");
        Optional<PersonDocument> empResult = personDocumentService.findByEmployeeId("EMP003");
        
        if (empResult.isPresent()) {
            PersonDocument person = empResult.get();
            log.info("找到员工: {} - {} ({})", person.getEmployeeId(), person.getName(), person.getDepartmentName());
        } else {
            log.error("未找到员工号为EMP003的员工");
        }
        
        assertTrue(empResult.isPresent(), "应该能找到员工号为EMP003的员工");
        assertEquals("王五", empResult.get().getName(), "员工姓名应该是王五");
    }

    /**
     * 创建人员文档对象
     */
    private PersonDocument createPerson(String name, String empId, String deptId, String deptName, String position) {
        log.debug("创建测试人员: name={}, employeeId={}, department={}", name, empId, deptName);
        
        String mobile = "138" + String.format("%08d", (int)(Math.random() * 100000000));
        String email = name + "@example.com";
        
        PersonDocument person = PersonDocument.builder()
                .name(name)
                .nameSort(name)
                .employeeId(empId)
                .departmentId(deptId)
                .departmentName(deptName)
                .position(position)
                .email(email)
                .mobile(mobile)
                .build();
        
        person.setId(UUID.randomUUID().toString());
        person.initCreate("test");
        
        log.debug("人员创建完成: ID={}, name={}, 创建时间={}", 
                 person.getId(), person.getName(), person.getCreateTime());
        
        return person;
    }
} 