package com.example.configuration;

/**
 * Created by gizmo on 15/8/12.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    public enum Origin {
        JD("京东"), TMALL_HK("天猫国际"), NETEASE_KOALA("网易考拉"), VIP("唯品会");

        private String value;

        Origin(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    public enum Category {
        Milk("奶粉"), JOHNSON("强生"), DYNAMIC("");

        private String value;

        Category(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    public enum ShopType {
        SELF("自营"), THIRD_PARTY("第三方");

        private String value;

        ShopType(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

}
