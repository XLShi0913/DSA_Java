package test;

import java.util.*;

public class TestLeetCode {
    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        threeSum(nums);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            twoSum(nums, nums[i], result);//逐次更新
        }
        return result;
    }
    //子方法，此方法用于解决两数问题
    private static void twoSum(int[] nums, int twoSum, List<List<Integer>> result){
        //输入的数组需要是有序数组
        int left = 0;
        int right = nums.length - 1;
        int sum;
        //需要找出所有的结果
        while(left < right){
            sum = nums[left] + nums[right];
            if (sum == twoSum) {
                ArrayList<Integer> resSingle = new ArrayList<>();
                resSingle.add(nums[left]);
                resSingle.add(nums[right]);
                resSingle.add(twoSum);
                result.add(resSingle);
                //此处需要过滤重复数据
                while(nums[left] == nums[left + 1]){left++;}
                while(nums[right] == nums[right - 1]){right--;}
                //更新数据
                left++;
                sum = nums[left] + nums[right];
            }else if (sum < twoSum) {
                left++;
                sum = nums[left] + nums[right];
            }else{
                right--;
                sum = nums[left] + nums[right];
            }
        }
    }
}
