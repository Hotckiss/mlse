import os

from github import Github

from crawler_config import CrawlerConfig


def save_file(content, pos, dirname='.JavaCC'):
    os.makedirs(os.path.dirname(f'{dirname}/{pos}_{content.name}'), exist_ok=True)
    with open(f'{dirname}/{pos}_{content.name}', 'wb') as f:
        f.write(content.decoded_content)


def crawl(query, config):
    pos = 0
    app = Github(config.API_TOKEN, per_page=config.PAGE_SIZE)
    for step in range(config.N_STEPS):
        current_min_file_size = config.MIN_FILE_SIZE + config.STEP_SIZE * step
        current_max_file_size = config.MIN_FILE_SIZE + config.STEP_SIZE * (step + 1) - 1
        print(f'size range: {current_min_file_size} .. {current_max_file_size}')
        code = app.search_code(query, sort='indexed', language='java',
                               size=f'{current_min_file_size}..{current_max_file_size}')
        for i in range(config.N_PAGES):
            current_page = code.get_page(i)
            print(f'page number: {i} ')
            print(f'page size: {len(current_page)}')
            if len(current_page) == 0:
                break
            for file in current_page:
                save_file(file, pos, dirname=config.DATASET_FOLDER)
                pos += 1
        if pos > config.MAX_FILE_COUNT:
            break


if __name__ == '__main__':
    config = CrawlerConfig()
    crawl(str(config.QUERY), config)
