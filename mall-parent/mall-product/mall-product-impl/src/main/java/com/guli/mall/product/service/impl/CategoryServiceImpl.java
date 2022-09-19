package com.guli.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.collect.Lists;
import com.guli.mall.product.converter.CategoryConverter;
import com.guli.mall.product.vo.CategoryVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.mall.common.utils.PageUtils;
import com.guli.mall.common.utils.Query;

import com.guli.mall.product.dao.CategoryDao;
import com.guli.mall.product.entity.CategoryEntity;
import com.guli.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public void removeMenuByIds(List<Long> idList) {
        // todo 1. 检查菜单是否被引用

        // 2. 逻辑删除
        super.removeByIds(idList);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryVO> listTree() {

        // 先查询出所有 category
        List<CategoryEntity> all = super.list();

        // 过滤出一级节点
        final List<CategoryVO> top = all.stream()
            .filter(categoryEntity -> Objects.equals(0L, categoryEntity.getParentCid()))
            .map(CategoryConverter::entityToVo)
            .collect(Collectors.toList());
        return sortAndFillChildren(top, all);
    }

    /**
     * 对某一层级的所有节点递归填充子节点
     * @param top
     * @param all
     */
    private List<CategoryVO> sortAndFillChildren(List<CategoryVO> top, List<CategoryEntity> all) {
        // 对每个节点逐一处理
        return top.stream()
            .sorted((l, r) -> {
                if (l.getSort() == null) {
                    return 1;
                }

                if (r.getSort() == null) {
                    return -1;
                }

                return l.getSort() - r.getSort();
            })
            .peek(root -> {
                // 从 all 中找到 root 的 children
                final List<CategoryVO> children = all.stream()
                    .filter(node -> Objects.equals(root.getCatId(), node.getParentCid()))
                    .map(CategoryConverter::entityToVo)
                    .collect(Collectors.toList());
                // 对 children 不为空则对每个节点递归处理
                if (!children.isEmpty()) {
                    sortAndFillChildren(children, all);
                }
                root.setChildren(children);
            }).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        final ArrayList<Integer> list = Lists.newArrayList(200, 100, 300);

        list.stream()
            .sorted(Integer::compare)
            .forEach(System.out::println);

        System.out.println(list);
    }
}