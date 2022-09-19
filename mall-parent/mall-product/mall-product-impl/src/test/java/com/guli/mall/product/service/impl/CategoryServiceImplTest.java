package com.guli.mall.product.service.impl;

import static org.junit.Assert.*;

import com.guli.mall.product.ProductBaseTest;
import com.guli.mall.product.service.CategoryService;
import com.guli.mall.product.vo.CategoryVO;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceImplTest extends ProductBaseTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void listTree() {

        final List<CategoryVO> categoryVOS = categoryService.listTree();

        System.out.println(categoryVOS);

    }
}