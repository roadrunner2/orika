/*
 * Orika - simpler, better and faster Java bean mapping
 *
 * Copyright (C) 2011-2013 Orika authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ma.glasnost.orika.test.inheritance;

import org.junit.Assert;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.test.MappingUtil;

import org.junit.Test;

public class UsedMappersTestCase {
    
    @Test
    public void testReuseOfMapper() {
        MapperFactory mapperFactory = MappingUtil.getMapperFactory();
        {
            mapperFactory.classMap(A.class, C.class)
                         .field("name", "nom")
                         .register();
        }
        
        {
            mapperFactory.classMap(B.class, D.class)
                    .use(A.class, C.class)
                    .field("age", "ages")
                    .register();
        }
        
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        
        B source = new B();
        source.setName("Israfil");
        source.setAge(1000);
        
        D target = mapperFacade.map(source, D.class);
        
        Assert.assertEquals(source.getName(), target.getNom());
        Assert.assertEquals(source.getAge(), target.getAges());
        
    }
    
    @Test
    public void testOneCallOfFieldMapping() {
        MapperFactory mapperFactory = MappingUtil.getMapperFactory();
        {
            mapperFactory.classMap(A.class, E.class)
                    .byDefault()
                    .register();
        }
        {
            mapperFactory.classMap(B.class, F.class)
                    .use(A.class, E.class)
                    .byDefault()
                    .register();
        }

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        
        B source = new B();
        source.setName("Israfil");
        source.setAge(1000);
        
        F target = mapperFacade.map(source, F.class);
        
        Assert.assertEquals(source.getName(), target.getName());
        Assert.assertEquals(source.getAge(), target.getAge());
        Assert.assertEquals(1, target.getNameCalls());
    }
    
    public static abstract class A {
        private String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
    }
    
    public static class B extends A {
        private int age;
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int ages) {
            this.age = ages;
        }
        
    }
    
    public static abstract class C {
        private String nom;
        
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
    }
    
    public static class D extends C {
        private int ages;
        
        public int getAges() {
            return ages;
        }
        
        public void setAges(int age) {
            this.ages = age;
        }
    }
    
    public static abstract class E {
        private String name;
        private int nameCalls;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
            nameCalls++;
        }
        
        public int getNameCalls() {
            return nameCalls;
        }
        
    }
    
    public static class F extends E {
        private int age;
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int ages) {
            this.age = ages;
        }
        
    }
}
