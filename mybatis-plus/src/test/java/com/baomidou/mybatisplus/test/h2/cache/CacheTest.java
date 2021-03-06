package com.baomidou.mybatisplus.test.h2.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.h2.cache.model.CacheModel;
import com.baomidou.mybatisplus.test.h2.cache.service.ICacheService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:h2/spring-cache-h2.xml"})
class CacheTest {

    @Autowired
    private ICacheService cacheService;

    @Test
    @Order(1)
    void testPageCache() {
        IPage<CacheModel> cacheModelIPage1 = cacheService.page(new Page<>(1, 3), new QueryWrapper<>());
        IPage<CacheModel> cacheModelIPage2 = cacheService.page(new Page<>(1, 3), new QueryWrapper<>());
        Assertions.assertEquals(cacheModelIPage1.getTotal(), cacheModelIPage2.getTotal());
        Assertions.assertEquals(cacheModelIPage1.getRecords().size(), cacheModelIPage2.getRecords().size());
        IPage<CacheModel> cacheModelIPage3 = cacheService.page(new Page<>(2, 3), new QueryWrapper<>());
        Assertions.assertEquals(cacheModelIPage1.getTotal(), cacheModelIPage3.getTotal());
        Assertions.assertEquals(cacheModelIPage3.getRecords().size(), 2);
        IPage<CacheModel> cacheModelIPage4 = cacheService.page(new Page<>(2, 3, false), new QueryWrapper<>());
        Assertions.assertEquals(cacheModelIPage4.getTotal(), 0L);
        Assertions.assertEquals(cacheModelIPage4.getRecords().size(), 2);
        IPage<CacheModel> cacheModelIPage5 = cacheService.page(new Page<>(2, 3, true), new QueryWrapper<>());
        Assertions.assertEquals(cacheModelIPage5.getTotal(), cacheModelIPage3.getTotal());
        Assertions.assertEquals(cacheModelIPage5.getRecords().size(), 2);
        IPage<CacheModel> cacheModelIPage6 = cacheService.page(new Page<>(1, 3, true), new QueryWrapper<CacheModel>().ge("id", 2L));
        Assertions.assertEquals(cacheModelIPage6.getTotal(), 4);
        Assertions.assertEquals(cacheModelIPage6.getRecords().size(), 3);
        IPage<CacheModel> cacheModelIPage7 = cacheService.page(new Page<>(1, 3, false), new QueryWrapper<CacheModel>().ge("id", 2L));
        Assertions.assertEquals(cacheModelIPage7.getTotal(), 0L);
        Assertions.assertEquals(cacheModelIPage7.getRecords().size(), 3);
        IPage<CacheModel> cacheModelIPage8 = cacheService.page(new Page<>(1, 3, false), new QueryWrapper<CacheModel>().ge("id", 3L));
        Assertions.assertEquals(cacheModelIPage8.getTotal(), 0L);
        Assertions.assertEquals(cacheModelIPage8.getRecords().size(), 3);
        cacheModelIPage8 = cacheService.page(new Page<>(1, 3, false), new QueryWrapper<CacheModel>().ge("id", 3L));
        Assertions.assertEquals(cacheModelIPage8.getTotal(), 0L);
        Assertions.assertEquals(cacheModelIPage8.getRecords().size(), 3);
        IPage<CacheModel> cacheModelIPage9 = cacheService.page(new Page<>(1, 3, true), new QueryWrapper<CacheModel>().ge("id", 3L));
        Assertions.assertEquals(cacheModelIPage9.getTotal(), 3L);
        Assertions.assertEquals(cacheModelIPage9.getRecords().size(), 3);
        cacheModelIPage9 = cacheService.page(new Page<>(1, 3, true), new QueryWrapper<CacheModel>().ge("id", 3L));
        Assertions.assertEquals(cacheModelIPage9.getTotal(), 3L);
        Assertions.assertEquals(cacheModelIPage9.getRecords().size(), 3);
    }

    @Test
    @Order(2)
    void testCleanBatchCache() {
        CacheModel model = new CacheModel("靓仔");
        cacheService.save(model);
        cacheService.getById(model.getId());
        cacheService.updateBatchById(Collections.singletonList(new CacheModel(model.getId(), "旺仔")));
        Assertions.assertEquals(cacheService.getById(model.getId()).getName(), "旺仔");
    }

    @Test
    @Order(3)
    void testBatchTransactionalClear1() {
        long id = cacheService.testBatchTransactionalClear1();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertEquals(cacheModel.getName(), "旺仔");
    }

    @Test
    @Order(4)
    void testBatchTransactionalClear2() {
        long id = cacheService.testBatchTransactionalClear2();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertEquals(cacheModel.getName(), "小红");
    }

    @Test
    @Order(5)
    void testBatchTransactionalClear3() {
        long id = cacheService.testBatchTransactionalClear3();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertEquals(cacheModel.getName(), "小红");
    }

    @Test
    @Order(6)
    void testBatchTransactionalClear4() {
        long id = cacheService.testBatchTransactionalClear4();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertEquals(cacheModel.getName(), "旺仔");
    }

    @Test
    @Order(7)
    void testBatchTransactionalClear5() {
        long id = cacheService.testBatchTransactionalClear5();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertNull(cacheModel);
    }

    @Test
    @Order(8)
    void testBatchTransactionalClear6() {
        long id = cacheService.testBatchTransactionalClear6();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertNull(cacheModel);
    }

    @Test
    @Order(9)
    void testBatchTransactionalClear7() {
        long id = cacheService.testBatchTransactionalClear7();
        CacheModel cacheModel = cacheService.getById(id);
        Assertions.assertNull(cacheModel);
    }
}
