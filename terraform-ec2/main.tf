terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  access_key = ".."
  secret_key = ".."
  region     = "us-east-1"
}

resource "aws_key_pair" "deployer" {
  key_name   = "test1-key"
  public_key = file("~/.ssh/terraform.pub")
}

resource "aws_security_group" "allow_tls" {
  name        = "my-jenkins-sg"
  description = "Allow SSH inbound traffic"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["178.251.110.181/32"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["178.251.110.181/32"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "my-jenkins-sg"
  }
}

resource "aws_instance" "app_server" {
  ami                    = "ami-026b57f3c383c2eec"
  instance_type          = "t2.micro"
  key_name               = "test1-key"
  vpc_security_group_ids = [aws_security_group.allow_tls.id]

  tags = {
    Name = "ExampleAppServerInstance"
  }

  user_data = file("./user_data.sh")
}