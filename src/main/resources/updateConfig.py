#!/usr/bin/python3

################################################################################
# This script allows to update old configuration files by adding missing fields
# set to their default value
################################################################################

import yaml
import argparse
import os

if __name__ == '__main__':
    
    basedir = os.path.dirname(os.path.abspath(__file__))
    
    # Parse arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--config', type=str, )
    args = parser.parse_args()
    if not (args.config):
        parser.error('No action requested, add -c')
        
    # Read config file
    with open(args.config, 'r') as f:
        config = yaml.safe_load(f)
            
    # Read default config file
    with open(basedir + '/default_config.yml', 'r') as f:
        defaultConfig = yaml.safe_load(f)
            
    # Set values existing in former file
    for key, value in config.items():
        try:
            if type(defaultConfig[key]) == type(value):
                defaultConfig[key] = value
        except KeyError:
            pass
        
    # Output the updated file
    with open(args.config, 'w') as f:
        f.write(yaml.dump(defaultConfig))
    
