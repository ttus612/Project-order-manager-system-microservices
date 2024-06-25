pipeline {
    agent any
    
    environment {
        DISCOVERY_SERVICE_DOCKER_HUB = 'ttus612/discovery_server'
        API_GATEWAY_DOCKER_HUB = 'ttus612/apigateway'
        JWT_SERVER_DOCKER_HUB = 'ttus612/jwt_server'
        ORDER_SERVICE_DOCKER_HUB = 'ttus612/order_server'
        PRODUCT_SERVICE_DOCKER_HUB = 'ttus612/product_server'
        SHIPPING_SERVICE_DOCKER_HUB = 'ttus612/shipping_server'
        SUPPLIER_SERVICE_DOCKER_HUB = 'ttus612/supplier_server'
        USER_SERVICE_DOCKER_HUB = 'ttus612/user_server'
        WAREHOUSE_SERVICE_DOCKER_HUB = 'ttus612/warehouse_server'
        IMAGE_TAG = 'v1.0'
    }
    stages {
        stage('Build') {
            parallel {
                stage('Build Discovery Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd DiscoveryServer && docker build -t $DISCOVERY_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $DISCOVERY_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build Api Gateway') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd ApiGateway && docker build -t $API_GATEWAY_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $API_GATEWAY_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build JWT Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd JWTServer && docker build -t $JWT_SERVER_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $JWT_SERVER_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build Order Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd OrderServer && docker build -t $ORDER_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $ORDER_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build Product Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd ProductServer && docker build -t $PRODUCT_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $PRODUCT_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build Shipping Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd ShippingServer && docker build -t $SHIPPING_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $SHIPPING_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                }
                stage('Build Supplier Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd SupplierServer && docker build -t $SUPPLIER_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $SUPPLIER_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                } 
                stage('Build User Server') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd UserServer && docker build -t $USER_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $USER_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                } 
                stage('Build Warehouse Serverr') {
                    steps {
                        withDockerRegistry(credentialsId: 'Account-DockerHUB', url: 'https://index.docker.io/v1/') {
                            sh 'cd WarehouseServer && docker build -t $WAREHOUSE_SERVICE_DOCKER_HUB:$IMAGE_TAG .'
                            sh 'docker push $WAREHOUSE_SERVICE_DOCKER_HUB:$IMAGE_TAG'
                        }
                    }
                } 
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker rm -f $(docker ps -a | grep -v "tkpm-group09-server-jenkins_main-mariadb-1" | grep -v "tkpm-group09-server-jenkins_main-redis-1" | grep -v "ttus612-jenkins" | cut -d " " -f1) || true'
                sh 'docker compose up -d'
                sh 'docker rmi $(docker images -f "dangling=true" -q) || true'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
